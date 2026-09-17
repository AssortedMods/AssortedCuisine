package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Milk in, butter out, but it is the one machine you can hurry: every right click while it is
 * working gives the handle a turn worth {@link #CHURN_TICKS}. Leave it alone and it still finishes,
 * just at its own pace - 1.12 had no wait at all, which made it a vending machine.
 */
public class ButterChurnBlock extends CuisineMachineBlock {

    /** Ticks of progress one turn of the handle is worth. */
    private static final int CHURN_TICKS = 60;

    public ButterChurnBlock(Properties props) {
        super(CuisineMachine.BUTTER_CHURN, props);
    }

    @Override
    protected InteractionResult workOn(BlockState state, Level level, BlockPos pos, Player player, CuisineMachineBlockEntity entity) {
        if (!level.isClientSide() && entity.advance(CHURN_TICKS)) {
            syncStage(level, pos, state, entity);
            level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.8F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        }

        // Swings the arm on the client too, which is what sells it as a handle.
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable ParticleOptions workingParticle() {
        return ParticleTypes.SPLASH;
    }
}
