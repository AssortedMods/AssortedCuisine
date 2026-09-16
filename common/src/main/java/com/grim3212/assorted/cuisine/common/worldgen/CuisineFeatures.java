package com.grim3212.assorted.cuisine.common.worldgen;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CuisineFeatures {

    public static final RegistryProvider<Feature<?>> FEATURES = RegistryProvider.create(Registries.FEATURE, Constants.MOD_ID);

    public static final IRegistryObject<Feature<NoneFeatureConfiguration>> COCOA_TREE = FEATURES.register("cocoa_tree", () -> new CocoaTreeFeature(NoneFeatureConfiguration.CODEC));

    public static void init() {
    }
}
