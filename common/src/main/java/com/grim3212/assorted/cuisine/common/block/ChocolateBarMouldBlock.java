package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Pour hot chocolate in, let it set, punch it out as bars. Setting is twice as fast on ice or snow,
 * which is the one reason the mould cares what it is standing on beyond needing support.
 */
public class ChocolateBarMouldBlock extends Block {

    public static final int DONE = 15;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, DONE);

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    public ChocolateBarMouldBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
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
        return direction == Direction.DOWN && !state.canSurvive(level, pos) ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);

        // Empty until chocolate is poured, and done until the bars are knocked out.
        if (stage == 0 || stage == DONE) {
            return;
        }

        BlockState below = level.getBlockState(pos.below());
        int step = below.is(BlockTags.ICE) || below.is(net.minecraft.world.level.block.Blocks.SNOW) || below.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK) ? 4 : 2;

        level.setBlock(pos, state.setValue(STAGE, Math.min(stage + step, DONE)), Block.UPDATE_CLIENTS);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(STAGE) != 0 || !stack.is(CuisineItems.HOT_CHOCOLATE.get())) {
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
        CuisineBlocks.popResult(level, pos, new ItemStack(CuisineItems.CHOCOLATE_BAR.get(), 2));
    }
}
