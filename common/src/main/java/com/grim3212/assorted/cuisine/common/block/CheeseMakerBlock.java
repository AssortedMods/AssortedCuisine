package com.grim3212.assorted.cuisine.common.block;

import net.minecraft.core.BlockPos;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Milk in, a block of cheese out. Right click with a milk bucket to start it, wait for it to
 * curdle, then punch it to collect.
 *
 * <p>The curdling is a plain blockstate counter driven by random ticks - there is no inventory and
 * nothing to sync, so this needs no block entity.
 */
public class CheeseMakerBlock extends Block {

    public static final int DONE = 15;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, DONE);

    public CheeseMakerBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);

        // Idle at 0 until milk is added, and done at DONE until it is collected. In between the
        // 1.12 curve moved 1 -> 9 -> 10 -> done, so filling one takes three random ticks.
        if (stage == 0 || stage == DONE) {
            return;
        }

        level.setBlock(pos, state.setValue(STAGE, stage == 9 ? 10 : Math.min(stage + 8, DONE)), Block.UPDATE_CLIENTS);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(STAGE) != 0 || !stack.is(LibCommonTags.Items.BUCKETS_MILK)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(STAGE, 1), Block.UPDATE_CLIENTS);
            CuisineBlocks.consumeContainer(player, stack);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide() || state.getValue(STAGE) != DONE) {
            return;
        }

        level.setBlock(pos, state.setValue(STAGE, 0), Block.UPDATE_ALL);
        CuisineBlocks.popResult(level, pos, new ItemStack(CuisineBlocks.CHEESE_BLOCK.get()));
    }
}
