package com.grim3212.assorted.health;

import com.grim3212.assorted.health.client.data.HealthItemModelProvider;
import com.grim3212.assorted.health.client.data.HealthLanguageProvider;
import com.grim3212.assorted.health.client.data.HealthManualProvider;
import com.grim3212.assorted.health.data.HealthAdvancements;
import com.grim3212.assorted.health.data.HealthBlockTagProvider;
import com.grim3212.assorted.health.data.HealthItemTagProvider;
import com.grim3212.assorted.health.data.HealthRecipes;
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
public class AssortedHealthNeoForge {

    public AssortedHealthNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        HealthCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new HealthBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new HealthItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(HealthRecipes::new)
                .advancements((registries, saver, existingFiles) -> new HealthAdvancements().generate(registries, saver)).addTo(event);

        generator.addProvider(event.includeClient(), new HealthItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new HealthLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new HealthManualProvider(packOutput));
    }
}
