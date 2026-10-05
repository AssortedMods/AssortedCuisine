package com.grim3212.assorted.dragonfruit;

import com.grim3212.assorted.dragonfruit.client.data.DragonFruitItemModelProvider;
import com.grim3212.assorted.dragonfruit.client.data.DragonFruitLanguageProvider;
import com.grim3212.assorted.dragonfruit.client.data.DragonFruitManualProvider;
import com.grim3212.assorted.dragonfruit.common.handlers.DragonFruitCompostables;
import com.grim3212.assorted.dragonfruit.data.DragonFruitAdvancements;
import com.grim3212.assorted.dragonfruit.data.DragonFruitBlockTagProvider;
import com.grim3212.assorted.dragonfruit.data.DragonFruitItemTagProvider;
import com.grim3212.assorted.dragonfruit.data.DragonFruitRecipes;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedDragonFruitNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedDragonFruitNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        DragonFruitCommonMod.init();
    }

    /** Anything that has to wait for the registries to be filled. */
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(DragonFruitCompostables::init);
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new DragonFruitBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new DragonFruitItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new DragonFruitRecipes.Runner(packOutput, lookupProvider));
        event.addProvider(new AdvancementProvider(packOutput, lookupProvider, List.of(new DragonFruitAdvancements())));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new DragonFruitItemModelProvider(packOutput));
        event.addProvider(new DragonFruitLanguageProvider(packOutput));
        event.addProvider(new DragonFruitManualProvider(packOutput));
    }
}
