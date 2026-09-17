package com.grim3212.assorted.cuisine.common.block.blockentity;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipe;
import com.grim3212.assorted.cuisine.common.block.CuisineMachineBlock;
import com.grim3212.assorted.cuisine.common.crafting.CuisineRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * What the cheese maker, the butter churn and the chocolate mould all need: an input to work on, an
 * output to take away, and somewhere for the bucket or bowl the recipe left behind.
 */
public class CuisineMachineBlockEntity extends BlockEntity implements WorldlyContainer {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    /** The empty bucket or bowl, handed back with the output rather than eaten. */
    public static final int SLOT_CONTAINER = 2;

    private static final int SIZE = 3;
    private static final int[] FROM_ABOVE = {SLOT_INPUT};
    private static final int[] FROM_BELOW = {SLOT_OUTPUT, SLOT_CONTAINER};
    private static final int[] FROM_SIDE = {SLOT_INPUT, SLOT_OUTPUT, SLOT_CONTAINER};

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    /** What the run under way will produce, and what it will hand back, once it finishes. */
    private ItemStack pendingResult = ItemStack.EMPTY;
    private ItemStack pendingContainer = ItemStack.EMPTY;

    private int progress;
    private int totalTime;

    public CuisineMachineBlockEntity(BlockPos pos, BlockState state) {
        super(CuisineBlockEntityTypes.MACHINE.get(), pos, state);
    }

    /**
     * One tick of work. Starts a run when there is something to start, and returns true on the tick
     * a run finishes so the block can play its chime.
     */
    public boolean serverTick(int speed) {
        if (this.pendingResult.isEmpty()) {
            this.tryStart();
            return false;
        }

        this.progress = Math.min(this.progress + speed, this.totalTime);
        this.setChanged();

        if (this.progress < this.totalTime) {
            return false;
        }

        this.finishRun();
        return true;
    }

    /**
     * Takes one input and books the run. Nothing starts unless both the result and whatever the
     * input leaves behind will fit when it is over, so a run can never finish with nowhere to go.
     */
    private void tryStart() {
        ItemStack input = this.items.get(SLOT_INPUT);
        CuisineMachineRecipe recipe = this.recipeFor(input);

        if (recipe == null) {
            return;
        }

        ItemStack result = recipe.getResultItem();
        ItemStack container = remainderFor(input);

        if (!this.fits(SLOT_OUTPUT, result) || !this.fits(SLOT_CONTAINER, container)) {
            return;
        }

        input.shrink(1);
        this.pendingResult = result;
        this.pendingContainer = container;
        this.progress = 0;
        this.totalTime = Math.max(1, recipe.getProcessTime());
        this.setChanged();
    }

    private void finishRun() {
        this.merge(SLOT_OUTPUT, this.pendingResult);
        this.merge(SLOT_CONTAINER, this.pendingContainer);

        this.pendingResult = ItemStack.EMPTY;
        this.pendingContainer = ItemStack.EMPTY;
        this.progress = 0;
        this.totalTime = 0;
        this.setChanged();
    }

    /** Adds progress out of turn - the churn's handle. Returns whether anything moved. */
    public boolean advance(int ticks) {
        if (!this.isWorking()) {
            return false;
        }

        this.progress = Math.min(this.progress + ticks, this.totalTime);
        this.setChanged();
        return true;
    }

    /**
     * Moves as much of {@code stack} into the input as will go, shrinking it, and answers whether
     * any of it went. Used by the player's hand, the dispenser and nothing else - a hopper goes
     * through the container methods.
     */
    public boolean fill(ItemStack stack) {
        if (stack.isEmpty() || !this.canPlaceItem(SLOT_INPUT, stack)) {
            return false;
        }

        ItemStack input = this.items.get(SLOT_INPUT);
        int room = (input.isEmpty() ? stack.getMaxStackSize() : input.getMaxStackSize() - input.getCount());
        int moved = Math.min(room, stack.getCount());

        if (moved <= 0) {
            return false;
        }

        if (input.isEmpty()) {
            this.items.set(SLOT_INPUT, stack.split(moved));
        } else if (ItemStack.isSameItemSameComponents(input, stack)) {
            input.grow(moved);
            stack.shrink(moved);
        } else {
            return false;
        }

        this.setChanged();
        return true;
    }

    /** Whatever is waiting to be taken out, emptied in one go. */
    public ItemStack takeOutput() {
        return this.removeItemNoUpdate(SLOT_OUTPUT);
    }

    public ItemStack takeContainer() {
        return this.removeItemNoUpdate(SLOT_CONTAINER);
    }

    public boolean hasCollectable() {
        return !this.items.get(SLOT_OUTPUT).isEmpty() || !this.items.get(SLOT_CONTAINER).isEmpty();
    }

    public boolean isWorking() {
        return !this.pendingResult.isEmpty();
    }

    /**
     * How far along, as one of the block's sixteen stages. A waiting output always reads full, so a
     * comparator can be wired to "there is something to take" and the model shows the done texture;
     * a run in progress reads somewhere between, and never 0 or {@code DONE}.
     */
    public int stage() {
        if (!this.items.get(SLOT_OUTPUT).isEmpty()) {
            return CuisineMachineBlock.DONE;
        }

        if (!this.isWorking()) {
            return 0;
        }

        return Mth.clamp(1 + this.progress * (CuisineMachineBlock.DONE - 1) / Math.max(1, this.totalTime), 1, CuisineMachineBlock.DONE - 1);
    }

    /** The recipe this machine would run for {@code stack}, or null for anything it does not take. */
    public @Nullable CuisineMachineRecipe recipeFor(ItemStack stack) {
        if (stack.isEmpty() || !(this.level instanceof ServerLevel serverLevel)) {
            return null;
        }

        if (!(this.getBlockState().getBlock() instanceof CuisineMachineBlock block)) {
            return null;
        }

        return serverLevel.recipeAccess()
                .getRecipeFor(CuisineRecipeTypes.type(block.getMachine()), new SingleRecipeInput(stack), serverLevel)
                .map(RecipeHolder::value)
                .orElse(null);
    }

    /**
     * What one of {@code stack} leaves behind, asked per stack rather than per item.
     */
    public static ItemStack remainderFor(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // One unit at a time: that is all a run consumes, whatever the slot happens to hold.
        NonNullList<ItemStack> remaining = CraftingRecipe.defaultCraftingReminder(CraftingInput.of(1, 1, List.of(stack.copyWithCount(1))));
        return remaining.isEmpty() ? ItemStack.EMPTY : remaining.get(0);
    }

    private boolean fits(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }

        ItemStack existing = this.items.get(slot);
        return existing.isEmpty()
                || (ItemStack.isSameItemSameComponents(existing, stack) && existing.getCount() + stack.getCount() <= existing.getMaxStackSize());
    }

    private void merge(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        ItemStack existing = this.items.get(slot);

        if (existing.isEmpty()) {
            this.items.set(slot, stack);
        } else {
            existing.grow(stack.getCount());
        }
    }

    // --- Container ---

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack taken = ContainerHelper.removeItem(this.items, slot, amount);

        if (!taken.isEmpty()) {
            this.setChanged();
        }

        return taken;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack taken = ContainerHelper.takeItem(this.items, slot);

        if (!taken.isEmpty()) {
            this.setChanged();
        }

        return taken;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    /** Only the input takes anything, and only what this machine has a recipe for. */
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == SLOT_INPUT && this.recipeFor(stack) != null;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    // --- WorldlyContainer ---

    @Override
    public int[] getSlotsForFace(Direction side) {
        return switch (side) {
            case UP -> FROM_ABOVE;
            case DOWN -> FROM_BELOW;
            default -> FROM_SIDE;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction face) {
        return this.canPlaceItem(slot, stack);
    }

    /** The input is not for taking back: a hopper must not steal the milk before it is used. */
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction face) {
        return slot != SLOT_INPUT;
    }

    // --- Saving ---

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.pendingResult = input.read("PendingResult", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.pendingContainer = input.read("PendingContainer", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.progress = input.getIntOr("Progress", 0);
        this.totalTime = input.getIntOr("TotalTime", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);

        if (!this.pendingResult.isEmpty()) {
            output.store("PendingResult", ItemStack.CODEC, this.pendingResult);
        }
        if (!this.pendingContainer.isEmpty()) {
            output.store("PendingContainer", ItemStack.CODEC, this.pendingContainer);
        }

        output.putInt("Progress", this.progress);
        output.putInt("TotalTime", this.totalTime);
    }

    /**
     * The stage the client needs is in the blockstate, but the slots ride along too so a future
     * renderer or a tooltip could show what is inside.
     */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * Spills everything when the block is broken, the half-finished run included: the input was
     * already taken off whoever put it in, so swallowing it would lose it.
     *
     * <p>It has to happen here: the block entity is already gone by
     * {@code affectNeighborsAfterRemoval}.
     */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level == null) {
            return;
        }

        Containers.dropContents(this.level, pos, this);
        Block.popResource(this.level, pos, this.pendingResult);
        Block.popResource(this.level, pos, this.pendingContainer);
        this.pendingResult = ItemStack.EMPTY;
        this.pendingContainer = ItemStack.EMPTY;
    }
}
