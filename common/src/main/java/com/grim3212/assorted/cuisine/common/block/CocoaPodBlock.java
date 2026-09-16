package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The fruit that hangs under a cocoa tree's leaves. Not vanilla's {@code cocoa}, which grows on the
 * side of jungle logs - this one hangs downwards and drops cocoa fruit rather than beans.
 */
public class CocoaPodBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4.5D, 4.0D, 4.5D, 11.5D, 16.0D, 11.5D);

    public CocoaPodBlock(Properties props) {
        super(props);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(BlockTags.LEAVES);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction == Direction.UP && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    // Pick block gives the fruit, not a block item - there is no block item for either of these.
    // NeoForge-only deprecation; the override point itself is still the right one.
    @SuppressWarnings("deprecation")
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(CuisineItems.COCOA_FRUIT.get());
    }
}
