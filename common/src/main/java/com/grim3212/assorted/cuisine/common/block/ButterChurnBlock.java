package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.core.BlockPos;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Right click with a milk bucket to churn, punch to knock the butter out. Unlike the cheese maker
 * there is no waiting - the churn is either full or it is not, so the 1.12 0/1 integer property is
 * a plain boolean here.
 */
public class ButterChurnBlock extends Block {

    public static final BooleanProperty FULL = BooleanProperty.create("full");

    public ButterChurnBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FULL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FULL);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(FULL)  || !stack.is(LibCommonTags.Items.BUCKETS_MILK)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(FULL, true), Block.UPDATE_CLIENTS);
            CuisineBlocks.consumeContainer(player, stack);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide() || !state.getValue(FULL)) {
            return;
        }

        level.setBlock(pos, state.setValue(FULL, false), Block.UPDATE_ALL);
        CuisineBlocks.popResult(level, pos, new ItemStack(CuisineItems.BUTTER.get(), 1 + level.getRandom().nextInt(3)));
    }
}
