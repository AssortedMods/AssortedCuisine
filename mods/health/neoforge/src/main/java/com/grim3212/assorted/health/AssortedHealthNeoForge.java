package com.grim3212.assorted.health;

import com.grim3212.assorted.health.client.data.HealthItemModelProvider;
import com.grim3212.assorted.health.client.data.HealthLanguageProvider;
import com.grim3212.assorted.health.client.data.HealthManualProvider;
import com.grim3212.assorted.health.common.handlers.HealthCompostables;
import com.grim3212.assorted.health.data.HealthAdvancements;
import com.grim3212.assorted.health.data.HealthBlockTagProvider;
import com.grim3212.assorted.health.data.HealthItemTagProvider;
import com.grim3212.assorted.health.data.HealthRecipes;
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
public class AssortedHealthNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedHealthNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        HealthCommonMod.init();
    }

    /** Anything that has to wait for the registries to be filled. */
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(HealthCompostables::init);
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new HealthBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new HealthItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new HealthRecipes.Runner(packOutput, lookupProvider));
        event.addProvider(new AdvancementProvider(packOutput, lookupProvider, List.of(new HealthAdvancements())));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new HealthItemModelProvider(packOutput));
        event.addProvider(new HealthLanguageProvider(packOutput));
        event.addProvider(new HealthManualProvider(packOutput));
    }
}
