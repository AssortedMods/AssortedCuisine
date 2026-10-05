package com.grim3212.assorted.kitchen;

import com.grim3212.assorted.kitchen.client.data.KitchenBlockstateProvider;
import com.grim3212.assorted.kitchen.client.data.KitchenItemModelProvider;
import com.grim3212.assorted.kitchen.client.data.KitchenLanguageProvider;
import com.grim3212.assorted.kitchen.client.data.KitchenManualProvider;
import com.grim3212.assorted.kitchen.common.handlers.KitchenCompostables;
import com.grim3212.assorted.kitchen.data.KitchenAdvancements;
import com.grim3212.assorted.kitchen.data.KitchenBlockLoot;
import com.grim3212.assorted.kitchen.data.KitchenBlockTagProvider;
import com.grim3212.assorted.kitchen.data.KitchenItemTagProvider;
import com.grim3212.assorted.kitchen.data.KitchenRecipes;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedKitchenNeoForge {

    /**
     * {@code FMLJavaModLoadingContext} is gone; the mod event bus and the mod container are injected
     * into the {@code @Mod} constructor instead.
     */
    public AssortedKitchenNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        KitchenCommonMod.init();
    }

    /** Anything that has to wait for the registries to be filled. */
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(KitchenCompostables::init);
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new KitchenBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new KitchenItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new KitchenRecipes.Runner(packOutput, lookupProvider));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(KitchenBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        event.addProvider(new AdvancementProvider(packOutput, lookupProvider, List.of(new KitchenAdvancements())));
    }

    /** Client datagen: models and the language file, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new KitchenBlockstateProvider(packOutput));
        event.addProvider(new KitchenItemModelProvider(packOutput));
        event.addProvider(new KitchenLanguageProvider(packOutput));
        event.addProvider(new KitchenManualProvider(packOutput));
    }
}
