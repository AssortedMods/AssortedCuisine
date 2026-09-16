package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.block.CocoaSaplingBlock;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.cuisine.gametest.CuisineTestSupport.*;

/**
 * The cocoa pod and the sapling it grows from. The pod hangs from leaves and falls when they go;
 * the sapling thickens once and then becomes a tree.
 */
final class CocoaTests {

    private CocoaTests() {
    }

    private static final int GROWTHS_PER_SOIL = 40;

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("cocoa_pod_needs_leaves", CocoaTests::cocoaPodNeedsLeaves);
        out.accept("cocoa_pod_falls_without_leaves", CocoaTests::cocoaPodFallsWithoutLeaves);
        out.accept("cocoa_sapling_grows_a_tree", CocoaTests::cocoaSaplingGrowsATree);
        out.accept("cocoa_tree_grows_on_every_soil", CocoaTests::cocoaTreeGrowsOnEverySoil);
    }

    private static void cocoaPodNeedsLeaves(GameTestHelper helper) {
        helper.setBlock(CENTRE.above(), Blocks.OAK_LEAVES);
        helper.setBlock(CENTRE, CuisineBlocks.COCOA_POD.get());

        helper.assertBlockPresent(CuisineBlocks.COCOA_POD.get(), CENTRE);
        helper.succeed();
    }

    private static void cocoaPodFallsWithoutLeaves(GameTestHelper helper) {
        helper.setBlock(CENTRE.above(), Blocks.OAK_LEAVES);
        helper.setBlock(CENTRE, CuisineBlocks.COCOA_POD.get());

        // Taking the leaves away leaves the pod with nothing to hang from.
        helper.setBlock(CENTRE.above(), Blocks.AIR);
        helper.assertBlockNotPresent(CuisineBlocks.COCOA_POD.get(), CENTRE);
        helper.succeed();
    }

    /**
     * The tree grows on grass, podzol, moss and mud as well as plain dirt, and not on sand. World
     * generation shares the check, and nearly every spot it tries is grass.
     */
    private static void cocoaTreeGrowsOnEverySoil(GameTestHelper helper) {
        BlockPos ground = new BlockPos(4, 0, 4);
        CocoaSaplingBlock sapling = CuisineBlocks.COCOA_SAPLING.get();

        // Several trees per soil: the tree's height and pods are random, and a bad roll once left a
        // pod where the trunk's bottom log belongs.
        for (int attempt = 0; attempt < GROWTHS_PER_SOIL; attempt++) {
        for (Block soil : List.of(Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.MOSS_BLOCK, Blocks.MUD, Blocks.SAND)) {
            clearAboveFloor(helper);
            helper.setBlock(ground, soil);
            helper.setBlock(CENTRE, sapling);

            for (int i = 0; i < 2; i++) {
                sapling.performBonemeal(helper.getLevel(), helper.getLevel().getRandom(), helper.absolutePos(CENTRE), helper.getBlockState(CENTRE));
            }

            String name = BuiltInRegistries.BLOCK.getKey(soil).getPath();
            if (soil == Blocks.SAND) {
                helper.assertFalse(helper.getBlockState(CENTRE).is(Blocks.OAK_LOG), "a cocoa tree grew on " + name);
            } else {
                helper.assertTrue(helper.getBlockState(CENTRE).is(Blocks.OAK_LOG), "a cocoa sapling on " + name + " did not grow into a trunk");
            }
        }
        }

        helper.succeed();
    }

    /** Clears whatever the last tree left, so the next one has the room it needs. */
    private static void clearAboveFloor(GameTestHelper helper) {
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                for (int y = 8; y >= 1; y--) {
                    helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
                }
            }
        }
    }

    private static void cocoaSaplingGrowsATree(GameTestHelper helper) {
        BlockPos ground = new BlockPos(4, 0, 4);
        helper.setBlock(ground, Blocks.DIRT);
        helper.setBlock(CENTRE, CuisineBlocks.COCOA_SAPLING.get());

        // Bonemeal rather than random ticks: growing on its own also needs light level 9, which is
        // about how bright the test box happens to be rather than about the growth being right.
        // Two rounds - one to thicken the sapling, one to sprout it.
        CocoaSaplingBlock sapling = CuisineBlocks.COCOA_SAPLING.get();
        for (int i = 0; i < 2; i++) {
            sapling.performBonemeal(helper.getLevel(), helper.getLevel().getRandom(), helper.absolutePos(CENTRE), helper.getBlockState(CENTRE));
        }

        helper.assertBlockPresent(Blocks.OAK_LOG, CENTRE);
        helper.succeed();
    }
}
