package com.grim3212.assorted.kitchen.api.crafting;

import com.grim3212.assorted.kitchen.common.crafting.KitchenRecipeTypes;
import com.grim3212.assorted.lib.manual.IManualRecipeProvider;
import com.grim3212.assorted.lib.manual.ManualRecipeView;
import com.grim3212.assorted.lib.manual.ManualSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * One item in, one item out, over {@link #getProcessTime()} ticks. All three machines share this
 * class and differ only by their {@link KitchenMachine}, which picks the type the block looks up.
 */
public class KitchenMachineRecipe implements Recipe<SingleRecipeInput>, IManualRecipeProvider {

    private final KitchenMachine machine;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int processTime;

    public KitchenMachineRecipe(KitchenMachine machine, String group, Ingredient ingredient, ItemStack result, int processTime) {
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
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeType<KitchenMachineRecipe> getType() {
        return KitchenRecipeTypes.type(this.machine);
    }

    @Override
    public RecipeSerializer<KitchenMachineRecipe> getSerializer() {
        return KitchenRecipeTypes.serializer(this.machine);
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    /**
     * Kept out of the recipe book. 1.21.1's book sorts every recipe it is sent into a tab from a fixed
     * enum of categories, and a machine has none, so the client would only log each one as an unknown
     * recipe category. JEI and the manual list them instead.
     */
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public ManualRecipeView manualView() {
        return ManualRecipeView.shaped(1, 1, List.of(ManualSlot.of(this.ingredient)), ManualSlot.of(this.getResultItem()));
    }

    public KitchenMachine getMachine() {
        return this.machine;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    /** A copy of the recipe's result, which needs no registries to read. */
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    public int getProcessTime() {
        return this.processTime;
    }
}
