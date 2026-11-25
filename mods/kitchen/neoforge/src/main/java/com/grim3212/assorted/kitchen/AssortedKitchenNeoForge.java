package com.grim3212.assorted.kitchen;

import com.grim3212.assorted.kitchen.client.data.KitchenBlockstateProvider;
import com.grim3212.assorted.kitchen.client.data.KitchenItemModelProvider;
import com.grim3212.assorted.kitchen.client.data.KitchenLanguageProvider;
import com.grim3212.assorted.kitchen.client.data.KitchenManualProvider;
import com.grim3212.assorted.kitchen.data.KitchenAdvancements;
import com.grim3212.assorted.kitchen.data.KitchenBlockLoot;
import com.grim3212.assorted.kitchen.data.KitchenBlockTagProvider;
import com.grim3212.assorted.kitchen.data.KitchenItemTagProvider;
import com.grim3212.assorted.kitchen.data.KitchenRecipes;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedKitchenNeoForge {

    public AssortedKitchenNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        KitchenCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new KitchenBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new KitchenItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(KitchenRecipes::new).blockLoot(KitchenBlockLoot::new)
                .advancements((registries, saver, existingFiles) -> new KitchenAdvancements().generate(registries, saver)).addTo(event);

        generator.addProvider(event.includeClient(), new KitchenBlockstateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new KitchenItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new KitchenLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new KitchenManualProvider(packOutput));
    }
}
