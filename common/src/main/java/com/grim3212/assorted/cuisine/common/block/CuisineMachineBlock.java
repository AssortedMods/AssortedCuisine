package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineBlockEntityTypes;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * A block you put one item into, wait at, and take something else out of. The cheese maker, the
 * butter churn and the chocolate mould are all this block with a different {@link CuisineMachine}.
 *
 * <p>What goes in and what comes out is a datapack recipe, so a pack can add goat cheese without a
 * new block. Progress lives in the block entity and is mirrored into {@link #STAGE} for the model
 * and the comparator; 1.12 kept it in the blockstate alone, which left nowhere to record what was
 * being made.
 */
public abstract class CuisineMachineBlock extends Block implements EntityBlock {

    public static final int DONE = 15;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, DONE);

    private final CuisineMachine machine;

    protected CuisineMachineBlock(CuisineMachine machine, Properties props) {
        super(props);
        this.machine = machine;
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CuisineMachineBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }

        return createTicker(type, CuisineBlockEntityTypes.MACHINE.get(), (tickLevel, pos, tickState, entity) -> {
            boolean finished = entity.serverTick(this.speedMultiplier(tickLevel, pos));

            syncStage(tickLevel, pos, tickLevel.getBlockState(pos), entity);

            if (finished) {
                finish(tickLevel, pos);
            }
        });
    }

    /**
     * Right click: take the finished result if there is one, otherwise start whatever the held item
     * is a recipe for. Collecting was a punch in 1.12, which also started breaking the block - in
     * creative that destroyed it before the cheese ever came out.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.interact(state, level, pos, player, stack);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return this.interact(state, level, pos, player, ItemStack.EMPTY);
    }

    private InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, ItemStack stack) {
        if (!(level.getBlockEntity(pos) instanceof CuisineMachineBlockEntity entity)) {
            return InteractionResult.PASS;
        }

        if (entity.hasCollectable()) {
            if (!level.isClientSide()) {
                this.collect(level, pos, state, player, entity);
            }

            return InteractionResult.SUCCESS;
        }

        if (entity.isWorking()) {
            return this.workOn(state, level, pos, player, entity);
        }

        if (stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        return this.load(state, level, pos, stack, entity);
    }

    /**
     * Hands what is waiting straight to the player rather than tossing it on the floor, the way
     * every other one-click collection in the game does.
     */
    private void collect(Level level, BlockPos pos, BlockState state, Player player, CuisineMachineBlockEntity entity) {
        CuisineBlocks.giveTo(player, entity.takeOutput());
        CuisineBlocks.giveTo(player, entity.takeContainer());

        syncStage(level, pos, state, entity);
        level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private InteractionResult load(BlockState state, Level level, BlockPos pos, ItemStack stack, CuisineMachineBlockEntity entity) {
        if (level.isClientSide()) {
            // The client cannot see this recipe type, so it assumes a full hand is worth a try and
            // lets the server correct it.
            return InteractionResult.SUCCESS;
        }

        if (!entity.fill(stack)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        syncStage(level, pos, state, entity);
        level.playSound(null, pos, this.insertSound(), SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.SUCCESS;
    }

    public CuisineMachine getMachine() {
        return this.machine;
    }

    /**
     * What a right click does while the machine is working. Nothing, unless the block has something
     * to turn - see the butter churn.
     */
    protected InteractionResult workOn(BlockState state, Level level, BlockPos pos, Player player, CuisineMachineBlockEntity entity) {
        return InteractionResult.PASS;
    }

    /** How many ticks of progress one tick is worth here; the mould reads the block below it. */
    protected int speedMultiplier(Level level, BlockPos pos) {
        return 1;
    }

    protected SoundEvent insertSound() {
        return SoundEvents.BUCKET_EMPTY;
    }

    /** The particle the block trails while working, or null for a machine that shows nothing. */
    protected @Nullable ParticleOptions workingParticle() {
        return null;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /** Zero while empty, then up with the work, so a comparator can wait for the cheese. */
    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return state.getValue(STAGE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        ParticleOptions particle = this.workingParticle();
        int stage = state.getValue(STAGE);

        if (particle == null || stage == 0 || stage == DONE || random.nextInt(3) != 0) {
            return;
        }

        level.addParticle(particle,
                pos.getX() + 0.25D + random.nextDouble() * 0.5D,
                pos.getY() + 0.9D,
                pos.getZ() + 0.25D + random.nextDouble() * 0.5D,
                0.0D, 0.01D, 0.0D);
    }

    /** Keeps the model and the comparator in step with the block entity. */
    protected static void syncStage(Level level, BlockPos pos, BlockState state, CuisineMachineBlockEntity entity) {
        int stage = entity.stage();

        if (state.getValue(STAGE) != stage) {
            level.setBlock(pos, state.setValue(STAGE, stage), Block.UPDATE_ALL);
        }
    }

    /** The one moment worth announcing: a burst of green and the brewing chime. */
    private static void finish(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.7F, 1.0F);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D, 8, 0.25D, 0.1D, 0.25D, 0.0D);
        }
    }

    /**
     * Only ticks the machine type, so a block whose entity is something else is left alone. Vanilla
     * spells this check out at every call site; it is the same one every time.
     */
    @SuppressWarnings("unchecked")
    private static <A extends BlockEntity, E extends BlockEntity> @Nullable BlockEntityTicker<A> createTicker(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }
}
