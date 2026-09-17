package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipe;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The recipes whose result is not obvious from the json: the ones that leave a worn tool behind
 * rather than consuming it. These go through {@code getRemainingItems}, which is the path the
 * crafting grid itself takes, so they cover whichever loader hook is doing the work.
 */
final class RecipeTests {

    private RecipeTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("bread_slice_wears_the_knife", RecipeTests::breadSliceWearsTheKnife);
        out.accept("spent_knife_is_not_returned", RecipeTests::spentKnifeIsNotReturned);
        out.accept("machine_recipes_load_without_complaint", RecipeTests::machineRecipesLoadWithoutComplaint);
    }

    /**
     * The machine recipes are not grid recipes, and must say so. A recipe that does not claim to be
     * special but publishes no placement is warned about and dropped from the ingredient index on
     * every load - this asserts the exact pair {@code RecipeManager} checks.
     */
    private static void machineRecipesLoadWithoutComplaint(GameTestHelper helper) {
        List<RecipeHolder<?>> machineRecipes = helper.getLevel().recipeAccess().getRecipes().stream()
                .filter(holder -> holder.value() instanceof CuisineMachineRecipe)
                .toList();

        helper.assertTrue(machineRecipes.size() == CuisineMachine.values().length,
                "expected one recipe per machine, found " + machineRecipes.size());

        for (RecipeHolder<?> holder : machineRecipes) {
            Recipe<?> recipe = holder.value();
            helper.assertFalse(!recipe.isSpecial() && recipe.placementInfo().isImpossibleToPlace(),
                    holder.id().identifier() + " would be warned about and ignored when recipes load");
        }

        helper.succeed();
    }

    private static void breadSliceWearsTheKnife(GameTestHelper helper) {
        ItemStack knife = new ItemStack(CuisineItems.KNIFE.get());
        CraftingInput input = CraftingInput.of(2, 1, List.of(new ItemStack(Items.BREAD), knife));
        CraftingRecipe recipe = recipeFor(helper, input, "bread and a knife");

        ItemStack result = recipe.assemble(input);
        helper.assertTrue(result.is(CuisineItems.BREAD_SLICE.get()), "crafting bread with a knife did not give bread slices");
        helper.assertTrue(result.getCount() == 2, "expected two bread slices, got " + result.getCount());

        ItemStack remainder = remainderAt(recipe, input, 1);
        helper.assertTrue(remainder.is(CuisineItems.KNIFE.get()), "the knife was consumed instead of being worn");
        helper.assertTrue(remainder.getOrDefault(DataComponents.DAMAGE, 0) == 1, "the knife did not take a point of damage");
        helper.succeed();
    }

    /** On its last point of durability the knife is used up rather than handed back broken. */
    private static void spentKnifeIsNotReturned(GameTestHelper helper) {
        ItemStack knife = new ItemStack(CuisineItems.KNIFE.get());
        knife.set(DataComponents.DAMAGE, knife.getOrDefault(DataComponents.MAX_DAMAGE, 0) - 1);

        CraftingInput input = CraftingInput.of(2, 1, List.of(new ItemStack(Items.BREAD), knife));
        CraftingRecipe recipe = recipeFor(helper, input, "bread and a worn out knife");

        helper.assertTrue(remainderAt(recipe, input, 1).isEmpty(), "a knife on its last use was still handed back");
        helper.succeed();
    }

    private static CraftingRecipe recipeFor(GameTestHelper helper, CraftingInput input, String what) {
        Optional<RecipeHolder<CraftingRecipe>> found = helper.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
        helper.assertTrue(found.isPresent(), "no crafting recipe matched " + what);
        return found.get().value();
    }

    private static ItemStack remainderAt(CraftingRecipe recipe, CraftingInput input, int slot) {
        NonNullList<ItemStack> remaining = recipe.getRemainingItems(input);
        return remaining.get(slot);
    }
}
