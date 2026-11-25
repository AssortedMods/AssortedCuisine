package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.kitchen.common.item.KitchenToolItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** The knife, whisk and mortar and pestle stay in the grid and wear a point per craft, in every recipe that takes one, including other mods' such as the dragon fruit's. */
final class KitchenToolTests {

    private KitchenToolTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_kitchen_tool_recipe_wears_the_tool", KitchenToolTests::everyKitchenToolRecipeWearsTheTool);
        out.accept("chocolate_bowl_hands_the_milk_bucket_back", KitchenToolTests::chocolateBowlHandsTheMilkBucketBack);
    }

    private static void everyKitchenToolRecipeWearsTheTool(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        int checked = 0;

        for (RecipeHolder<?> holder : helper.getLevel().getRecipeManager().getRecipes()) {
            if (!(holder.value() instanceof CraftingRecipe recipe)) {
                continue;
            }
            List<Ingredient> ingredients = recipe.getIngredients().stream().filter(ingredient -> !ingredient.isEmpty()).toList();
            List<ItemStack> grid = new ArrayList<>();
            int toolSlot = -1;
            for (Ingredient ingredient : ingredients) {
                // A tag of knives may hold another mod's first, so the kitchen's own is used where it fits.
                Item item = Arrays.stream(ingredient.getItems()).map(ItemStack::getItem)
                        .filter(candidate -> candidate instanceof KitchenToolItem).findFirst()
                        .orElseGet(() -> Arrays.stream(ingredient.getItems()).findFirst().map(ItemStack::getItem).orElse(Items.AIR));
                if (item instanceof KitchenToolItem) {
                    toolSlot = grid.size();
                }
                grid.add(new ItemStack(item));
            }
            if (toolSlot < 0 || recipe.isSpecial()) {
                continue;
            }
            checked++;

            CraftingInput input = CraftingInput.of(grid.size(), 1, grid);
            String id = holder.id().toString();
            if (!recipe.matches(input, helper.getLevel())) {
                // A shaped recipe needs its own layout, which the existing knife test covers.
                continue;
            }
            ItemStack tool = grid.get(toolSlot);
            List<ItemStack> left = recipe.getRemainingItems(input);
            if (left.stream().noneMatch(stack -> stack.is(tool.getItem()) && stack.getOrDefault(DataComponents.DAMAGE, 0) == 1)) {
                problems.add(id + " left " + left + ", not a " + tool.getHoverName().getString() + " worn by one point");
            }
        }

        helper.assertTrue(checked >= 3, "only " + checked + " recipes take a kitchen tool");
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    private static void chocolateBowlHandsTheMilkBucketBack(GameTestHelper helper) {
        ItemStack dust = new ItemStack(KitchenItems.COCOA_DUST.get());
        List<ItemStack> grid = List.of(ItemStack.EMPTY, dust, ItemStack.EMPTY,
                dust, new ItemStack(Items.SUGAR), dust,
                ItemStack.EMPTY, new ItemStack(Items.MILK_BUCKET), ItemStack.EMPTY);
        CraftingInput input = CraftingInput.of(3, 3, grid);
        CraftingRecipe recipe = helper.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel())
                .orElseThrow(() -> new GameTestAssertException("cocoa dust, sugar and a milk bucket make nothing")).value();
        helper.assertTrue(recipe.assemble(input, helper.getLevel().registryAccess()).is(KitchenItems.CHOCOLATE_BOWL.get()), "cocoa dust, sugar and milk did not make a chocolate bowl");
        helper.assertTrue(recipe.getRemainingItems(input).stream().anyMatch(stack -> stack.is(Items.BUCKET)), "the milk bucket was used up rather than handed back empty");
        helper.succeed();
    }
}
