package com.grim3212.assorted.cuisine.api.crafting;

import com.grim3212.assorted.cuisine.common.crafting.CuisineRecipeTypes;
import com.grim3212.assorted.lib.manual.IManualRecipeProvider;
import com.grim3212.assorted.lib.manual.ManualRecipeView;
import com.grim3212.assorted.lib.manual.ManualSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * One item in, one item out, over {@link #getProcessTime()} ticks. All three machines share this
 * class and differ only by their {@link CuisineMachine}, which picks the type the block looks up.
 */
public class CuisineMachineRecipe implements Recipe<SingleRecipeInput>, IManualRecipeProvider {

    private final CuisineMachine machine;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    private final int processTime;

    public CuisineMachineRecipe(CuisineMachine machine, String group, Ingredient ingredient, ItemStackTemplate result, int processTime) {
        this.machine = machine;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.processTime = processTime;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return this.result.create();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return CuisineRecipeTypes.type(this.machine);
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return CuisineRecipeTypes.serializer(this.machine);
    }

    @Override
    public String group() {
        return this.group;
    }

    /**
     * Special, meaning it is not laid out in a grid and the recipe book has no business with it.
     * Without this, {@code RecipeManager} sees a placeable recipe it cannot place and logs
     * "can't be placed due to empty ingredients and will be ignored" for every one of these on
     * every load.
     */
    @Override
    public boolean isSpecial() {
        return true;
    }

    /** Not placeable: it has no display, so the recipe book never offers to lay it out. */
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of();
    }

    /** Required of every recipe, but unreachable: with no display it never enters the book. */
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return CuisineRecipeTypes.MACHINE_CATEGORY.get();
    }

    /** Nothing to toast about: the book never learns these, so it has no notification to show. */
    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public ManualRecipeView manualView() {
        return ManualRecipeView.shaped(1, 1, List.of(ManualSlot.of(this.ingredient.display())),
                ManualSlot.of(new SlotDisplay.ItemStackSlotDisplay(this.result)));
    }

    public CuisineMachine getMachine() {
        return this.machine;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    /** The result as a template: a stack cannot be built before item components are bound. */
    public ItemStackTemplate getResultTemplate() {
        return this.result;
    }

    public ItemStack getResultItem() {
        return this.result.create();
    }

    public int getProcessTime() {
        return this.processTime;
    }
}
