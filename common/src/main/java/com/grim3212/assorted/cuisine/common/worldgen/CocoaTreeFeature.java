package com.grim3212.assorted.cuisine.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Places a {@link CocoaTreeGrower} tree during world generation. */
public class CocoaTreeFeature extends Feature<NoneFeatureConfiguration> {

    public CocoaTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return CocoaTreeGrower.growTree(context.level(), context.random(), context.origin());
    }
}
