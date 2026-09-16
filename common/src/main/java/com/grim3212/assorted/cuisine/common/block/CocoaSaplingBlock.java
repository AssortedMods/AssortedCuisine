package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.cuisine.common.worldgen.CocoaTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.serialization.MapCodec;

/**
 * Grows into a cocoa tree. Like a vanilla sapling it takes two steps - one to thicken, one to
 * sprout - which is what STAGE counts.
 *
 * <p>Bonemeal works through {@link BonemealableBlock} rather than the 1.12 BonemealEvent; both
 * loaders call the interface, so there is no event to hook.
 */
public class CocoaSaplingBlock extends VegetationBlock implements BonemealableBlock {

    public static final MapCodec<CocoaSaplingBlock> CODEC = simpleCodec(CocoaSaplingBlock::new);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);

    private static final VoxelShape SHAPE = Block.box(1.5D, 0.0D, 1.5D, 14.5D, 13.0D, 14.5D);

    public CocoaSaplingBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected MapCodec<CocoaSaplingBlock> codec() {
        return CODEC;
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
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(7) == 0) {
            this.grow(level, random, pos, state);
        }
    }

    private void grow(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.setValue(STAGE, 1), Block.UPDATE_INVISIBLE);
            return;
        }

        // The trunk starts where the sapling stands, so it has to come out of the way first -
        // otherwise the tree sees its own sapling blocking the column and gives up.
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_INVISIBLE);

        if (!CocoaTreeGrower.growTree(level, random, pos)) {
            level.setBlock(pos, state, Block.UPDATE_INVISIBLE);
        }
    }

    // Pick block gives the fruit, not a block item - there is no block item for either of these.
    // NeoForge-only deprecation; the override point itself is still the right one.
    @SuppressWarnings("deprecation")
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(CuisineItems.COCOA_FRUIT.get());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.grow(level, random, pos, state);
    }
}
