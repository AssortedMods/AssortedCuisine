package com.grim3212.assorted.sodas;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.sodas.client.data.SodasItemModelProvider;
import com.grim3212.assorted.sodas.client.data.SodasLanguageProvider;
import com.grim3212.assorted.sodas.client.data.SodasManualProvider;
import com.grim3212.assorted.sodas.data.SodasAdvancements;
import com.grim3212.assorted.sodas.data.SodasBlockTagProvider;
import com.grim3212.assorted.sodas.data.SodasItemTagProvider;
import com.grim3212.assorted.sodas.data.SodasRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedSodasNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedSodasNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        SodasCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new SodasBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new SodasItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new SodasRecipes.Runner(packOutput, lookupProvider));
        event.addProvider(new AdvancementProvider(packOutput, lookupProvider, List.of(new SodasAdvancements())));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new SodasItemModelProvider(packOutput));
        event.addProvider(new SodasLanguageProvider(packOutput));
        event.addProvider(new SodasManualProvider(packOutput));
    }
}
