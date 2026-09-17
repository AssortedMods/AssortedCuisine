package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Pour hot chocolate in, let it set, right click it out as bars. Setting is twice as fast on ice or
 * snow, which is the one reason the mould cares what it is standing on beyond needing support.
 */
public class ChocolateBarMouldBlock extends CuisineMachineBlock {

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    public ChocolateBarMouldBlock(Properties props) {
        super(CuisineMachine.CHOCOLATE_MOULD, props);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    /** Standing it on something cold sets the chocolate in half the time. */
    @Override
    protected int speedMultiplier(Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlockTags.ICE) || below.is(Blocks.SNOW) || below.is(Blocks.SNOW_BLOCK) ? 2 : 1;
    }

    /** It went in hot, so it steams while it cools. */
    @Override
    protected @Nullable ParticleOptions workingParticle() {
        return ParticleTypes.SMOKE;
    }
}
