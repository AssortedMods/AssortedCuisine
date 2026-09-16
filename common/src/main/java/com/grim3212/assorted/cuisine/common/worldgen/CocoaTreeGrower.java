package com.grim3212.assorted.cuisine.common.worldgen;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The cocoa tree itself: an oak-shaped trunk and canopy with cocoa pods hanging under some of the
 * leaves. Shared by the sapling and by the world generation feature, which is why the shape lives
 * here rather than inside either one.
 */
public class CocoaTreeGrower {

    private static final int MIN_HEIGHT = 4;
    private static final int HEIGHT_VARIATION = 3;

    /** One pod for roughly every sixth leaf block, as in 1.12. */
    private static final int POD_CHANCE = 6;

    public static boolean growTree(LevelAccessor level, RandomSource random, BlockPos origin) {
        int height = random.nextInt(HEIGHT_VARIATION) + MIN_HEIGHT;

        if (!canFit(level, origin, height)) {
            return false;
        }

        BlockState log = Blocks.OAK_LOG.defaultBlockState();
        BlockState leaves = Blocks.OAK_LEAVES.defaultBlockState();
        BlockState pod = CuisineBlocks.COCOA_POD.get().defaultBlockState();

        level.setBlock(origin.below(), Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);

        // Canopy first, so the trunk can overwrite any leaf that lands in its column.
        for (int y = origin.getY() + height - 3; y <= origin.getY() + height; y++) {
            int fromTop = y - (origin.getY() + height);
            int radius = 1 - fromTop / 2;

            for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++) {
                for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                    int dx = Math.abs(x - origin.getX());
                    int dz = Math.abs(z - origin.getZ());

                    // Round the corners off, except on the widest ring.
                    if (dx == radius && dz == radius && (random.nextInt(2) == 0 || fromTop == 0)) {
                        continue;
                    }

                    BlockPos pos = new BlockPos(x, y, z);
                    if (!isReplaceable(level, pos)) {
                        continue;
                    }

                    level.setBlock(pos, leaves, Block.UPDATE_CLIENTS);

                    // A pod hangs below a leaf, never below another pod.
                    BlockPos below = pos.below();
                    if (random.nextInt(POD_CHANCE) == 0 && isReplaceable(level, below) && !level.getBlockState(below).is(CuisineBlocks.COCOA_POD.get())) {
                        level.setBlock(below, pod, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }

        for (int y = 0; y < height; y++) {
            BlockPos pos = origin.above(y);
            if (isReplaceable(level, pos) || level.getBlockState(pos).is(BlockTags.LEAVES)) {
                level.setBlock(pos, log, Block.UPDATE_CLIENTS);
            }
        }

        return true;
    }

    private static boolean canFit(LevelAccessor level, BlockPos origin, int height) {
        if (!level.getBlockState(origin.below()).is(BlockTags.DIRT)) {
            return false;
        }

        if (origin.getY() + height + 1 >= level.getMaxY()) {
            return false;
        }

        for (int y = origin.getY(); y <= origin.getY() + height + 1; y++) {
            // The trunk needs its own column clear; the canopy needs one block either side.
            int radius = y == origin.getY() ? 0 : (y >= origin.getY() + height - 1 ? 2 : 1);

            for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++) {
                for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!isReplaceable(level, pos) && !level.getBlockState(pos).is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean isReplaceable(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }
}
