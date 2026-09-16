package com.grim3212.assorted.cuisine;

import com.grim3212.assorted.cuisine.data.CuisineBlockLoot;
import com.grim3212.assorted.cuisine.data.CuisineBlockTagProvider;
import com.grim3212.assorted.cuisine.data.CuisineGenData;
import com.grim3212.assorted.cuisine.data.CuisineItemTagProvider;
import com.grim3212.assorted.cuisine.data.CuisineRecipes;
import com.grim3212.assorted.lib.data.FabricBlockTagProvider;
import com.grim3212.assorted.lib.data.FabricConditionalRecipeProvider;
import com.grim3212.assorted.lib.data.FabricDatapackRegistryProvider;
import com.grim3212.assorted.lib.data.FabricItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

/**
 * Fabric's half of datagen: the recipes, tags and loot that carry Fabric's own load conditions.
 * Models and language come from the NeoForge datagen, which writes into common for both loaders.
 */
public class AssortedCuisineFabricDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        FabricBlockTagProvider provider = pack.addProvider((output, registriesFuture) -> new FabricBlockTagProvider(output, registriesFuture, new CuisineBlockTagProvider(output, registriesFuture)));
        pack.addProvider((output, registriesFuture) -> new FabricItemTagProvider(output, registriesFuture, provider.contentsGetter(), new CuisineItemTagProvider(output, registriesFuture, provider.contentsGetter())));

        // Recipe providers are not data providers any more - the Runner owns the output.
        pack.addProvider((output, registriesFuture) -> new FabricConditionalRecipeProvider(output, registriesFuture, new CuisineRecipes.Runner(output, registriesFuture)));
        pack.addProvider((output, registriesFuture) -> new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(CuisineBlockLoot::new, LootContextParamSets.BLOCK)), registriesFuture));

        pack.addProvider((output, registriesFuture) -> new FabricDatapackRegistryProvider(output, registriesFuture, Constants.MOD_ID, getGenData()));
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        getGenData().addEntries(registryBuilder);
    }

    private CuisineGenData genData;

    private CuisineGenData getGenData() {
        if (this.genData == null) {
            this.genData = new CuisineGenData();
        }
        return this.genData;
    }
}
