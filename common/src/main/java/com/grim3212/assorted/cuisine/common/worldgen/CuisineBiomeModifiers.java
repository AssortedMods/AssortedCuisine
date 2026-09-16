package com.grim3212.assorted.cuisine.common.worldgen;

import com.grim3212.assorted.cuisine.CuisineCommonMod;
import com.grim3212.assorted.cuisine.data.CuisineGenData;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.platform.services.IWorldGenHelper;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

public class CuisineBiomeModifiers {

    public static void init() {
        // 1.12 generated cocoa trees in every overworld chunk and let the "must stand on dirt"
        // check do the filtering, so the same tag keeps the same places in range.
        Services.WORLD_GEN.addFeatureToBiomes(matchesTag(BiomeTags.IS_OVERWORLD), GenerationStep.Decoration.VEGETAL_DECORATION, CuisineGenData.COCOA_TREE_KEY);
    }

    /**
     * The config is read inside the predicate rather than around the registration: biome predicates
     * are asked once per biome as the world loads, which is long after the config exists, whereas
     * this runs during mod construction, when reading one throws.
     */
    private static IWorldGenHelper.BiomePredicate matchesTag(TagKey<Biome> tag) {
        return (resourceLocation, biome) -> CuisineCommonMod.COMMON_CONFIG.chocolateEnabled.get() && CuisineCommonMod.COMMON_CONFIG.generateCocoaTrees.get() && biome.is(tag);
    }
}
