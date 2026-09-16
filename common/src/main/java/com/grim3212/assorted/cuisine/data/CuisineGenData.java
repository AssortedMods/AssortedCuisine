package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.worldgen.CuisineFeatures;
import com.grim3212.assorted.lib.data.LibDatapackRegistryProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuisineGenData extends LibDatapackRegistryProvider {

    public static final Identifier COCOA_TREE_KEY = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cocoa_tree");

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey(Identifier key) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, key);
    }

    private static Map<Identifier, ConfiguredFeature<?, ?>> getConfiguredFeatures() {
        Map<Identifier, ConfiguredFeature<?, ?>> map = new HashMap<>();
        map.put(COCOA_TREE_KEY, new ConfiguredFeature<>(CuisineFeatures.COCOA_TREE.get(), NoneFeatureConfiguration.INSTANCE));
        return map;
    }

    private static Map<Identifier, PlacedFeature> getPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        Map<Identifier, PlacedFeature> map = new HashMap<>();

        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = context.lookup(Registries.CONFIGURED_FEATURE);
        map.put(COCOA_TREE_KEY, new PlacedFeature(holderGetter.getOrThrow(configuredFeatureResourceKey(COCOA_TREE_KEY)), treePlacement(12)));

        return map;
    }

    private static List<PlacementModifier> treePlacement(int rarity) {
        return List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

    @Override
    public List<ResourceKey<? extends Registry<?>>> registries() {
        return List.of(Registries.CONFIGURED_FEATURE, Registries.PLACED_FEATURE);
    }

    @Override
    public void addEntries(RegistrySetBuilder builder) {
        builder.add(Registries.CONFIGURED_FEATURE, context -> {
            CuisineGenData.getConfiguredFeatures().forEach((r, f) -> {
                context.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, r), f);
            });
        });

        builder.add(Registries.PLACED_FEATURE, context -> {
            CuisineGenData.getPlacedFeatures(context).forEach((r, f) -> {
                context.register(ResourceKey.create(Registries.PLACED_FEATURE, r), f);
            });
        });
    }
}
