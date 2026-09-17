package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Pour hot chocolate in, let it set, right click it out as bars.
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

    /**
     * A tick of work per tick, and one more for every cold block packed against its sides. Ringed
     * with ice on all four it sets five times as fast, which is the reason to build the ring.
     */
    @Override
    protected int speedMultiplier(Level level, BlockPos pos) {
        int speed = 1;

        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (isCold(level.getBlockState(pos.relative(side)))) {
                speed++;
            }
        }

        return speed;
    }

    /** Every kind of ice and snow. */
    private static boolean isCold(BlockState state) {
        return state.is(BlockTags.ICE) || state.is(BlockTags.SNOW);
    }

    /** It went in hot, so it steams while it cools. */
    @Override
    protected @Nullable ParticleOptions workingParticle() {
        return ParticleTypes.SMOKE;
    }
}
