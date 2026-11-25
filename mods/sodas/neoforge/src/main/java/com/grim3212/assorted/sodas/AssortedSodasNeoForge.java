package com.grim3212.assorted.sodas;

import com.grim3212.assorted.sodas.client.data.SodasItemModelProvider;
import com.grim3212.assorted.sodas.client.data.SodasLanguageProvider;
import com.grim3212.assorted.sodas.client.data.SodasManualProvider;
import com.grim3212.assorted.sodas.data.SodasAdvancements;
import com.grim3212.assorted.sodas.data.SodasBlockTagProvider;
import com.grim3212.assorted.sodas.data.SodasItemTagProvider;
import com.grim3212.assorted.sodas.data.SodasRecipes;
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
public class AssortedSodasNeoForge {

    public AssortedSodasNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        SodasCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new SodasBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new SodasItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(SodasRecipes::new)
                .advancements((registries, saver, existingFiles) -> new SodasAdvancements().generate(registries, saver)).addTo(event);

        generator.addProvider(event.includeClient(), new SodasItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new SodasLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new SodasManualProvider(packOutput));
    }
}
