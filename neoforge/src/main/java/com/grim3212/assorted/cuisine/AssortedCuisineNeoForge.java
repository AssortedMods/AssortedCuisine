package com.grim3212.assorted.cuisine;

import com.grim3212.assorted.cuisine.client.data.CuisineBlockstateProvider;
import com.grim3212.assorted.cuisine.client.data.CuisineItemModelProvider;
import com.grim3212.assorted.cuisine.client.data.CuisineLanguageProvider;
import com.grim3212.assorted.cuisine.client.data.CuisineManualProvider;
import com.grim3212.assorted.cuisine.data.CuisineBlockLoot;
import com.grim3212.assorted.cuisine.data.CuisineBlockTagProvider;
import com.grim3212.assorted.cuisine.data.CuisineItemTagProvider;
import com.grim3212.assorted.cuisine.data.CuisineRecipes;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedCuisineNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedCuisineNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        CuisineCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new CuisineBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new CuisineItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new CuisineRecipes.Runner(packOutput, lookupProvider));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(CuisineBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
    }

    /** Client datagen: models and the language file, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new CuisineBlockstateProvider(packOutput));
        event.addProvider(new CuisineItemModelProvider(packOutput));
        event.addProvider(new CuisineLanguageProvider(packOutput));
        event.addProvider(new CuisineManualProvider(packOutput));
    }
}
