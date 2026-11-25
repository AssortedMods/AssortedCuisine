package com.grim3212.assorted.kitchen.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachine;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachineRecipe;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    /** The one strip all three machines are drawn on, in both the manual and JEI. */
    private static final String TEXTURE_ID = Constants.MOD_ID + ":textures/gui/container/cuisine_machine.png";
    private static final String MACHINE_TEXTURE = "/assets/" + TEXTURE_ID.replace(':', '/');

    /** The line the machine slot carries in the book, the same one JEI's catalyst slot uses. */
    private static final String MADE_IN = "tooltip." + Constants.MOD_ID + ".made_in";

    private RecipeTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("jei_plugin_is_registered_on_fabric", RecipeTests::jeiPluginIsRegisteredOnFabric);
        out.accept("bread_slice_wears_the_knife", RecipeTests::breadSliceWearsTheKnife);
        out.accept("spent_knife_is_not_returned", RecipeTests::spentKnifeIsNotReturned);
        out.accept("machine_recipes_load_without_complaint", RecipeTests::machineRecipesLoadWithoutComplaint);
        out.accept("machine_types_have_their_own_gui", RecipeTests::machineTypesHaveTheirOwnGui);
    }

    /**
     * The machine recipes are not grid recipes, and must say so. The client's recipe book sorts every
     * recipe that is neither special nor incomplete into a tab, and has none for a machine, so it would
     * log each one as an unknown category - this asserts the exact pair {@code ClientRecipeBook} checks.
     */
    private static void machineRecipesLoadWithoutComplaint(GameTestHelper helper) {
        List<RecipeHolder<?>> machineRecipes = helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.value() instanceof KitchenMachineRecipe)
                .toList();

        helper.assertTrue(machineRecipes.size() == KitchenMachine.values().length,
                "expected one recipe per machine, found " + machineRecipes.size());

        for (RecipeHolder<?> holder : machineRecipes) {
            Recipe<?> recipe = holder.value();
            helper.assertTrue(recipe.isSpecial() || recipe.isIncomplete(),
                    holder.id() + " would be sorted into the recipe book, which has no tab for it, and logged as an unknown category");
        }

        helper.succeed();
    }

    /**
     * Both the manual and JEI draw a machine process on its own strip. The manual finds it by recipe
     * type and silently falls back to the crafting table when there is none - which is how these
     * came to be drawn as crafting grids - and JEI hardcodes the same texture, so a layout naming a
     * file that is not there breaks one or both without anything being logged.
     */
    private static void machineTypesHaveTheirOwnGui(GameTestHelper helper) {
        helper.assertTrue(KitchenMachine.class.getResourceAsStream(MACHINE_TEXTURE) != null,
                "no " + MACHINE_TEXTURE + ", which both the manual layouts and JEI draw the strip from");

        for (KitchenMachine machine : KitchenMachine.values()) {
            String path = "/assets/" + Constants.MOD_ID + "/manual/recipe_layouts/" + machine.getName() + ".json";

            try (InputStream in = KitchenMachine.class.getResourceAsStream(path)) {
                helper.assertTrue(in != null,
                        "no manual layout for " + machine.getName() + ", so the manual would draw it as a crafting recipe");

                JsonObject layout = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
                helper.assertTrue(TEXTURE_ID.equals(layout.get("texture").getAsString()),
                        machine.getName() + " is drawn from " + layout.get("texture").getAsString()
                                + " while JEI draws " + TEXTURE_ID + ", so the book and JEI would not match");

                // Without it the machine is an unexplained item beside an arrow; JEI names it here.
                JsonObject station = layout.getAsJsonArray("extras").get(0).getAsJsonObject();
                helper.assertTrue(station.has("tooltip") && MADE_IN.equals(station.get("tooltip").getAsString()),
                        machine.getName() + " does not put " + MADE_IN + " on its machine slot, so the book "
                                + "would not say what makes this while JEI does");
            } catch (IOException e) {
                helper.fail("could not read " + path + ": " + e.getMessage());
            }
        }

        helper.succeed();
    }

    private static void breadSliceWearsTheKnife(GameTestHelper helper) {
        ItemStack knife = new ItemStack(KitchenItems.KNIFE.get());
        CraftingInput input = CraftingInput.of(2, 1, List.of(new ItemStack(Items.BREAD), knife));
        CraftingRecipe recipe = recipeFor(helper, input, "bread and a knife");

        ItemStack result = recipe.assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue(result.is(KitchenItems.BREAD_SLICE.get()), "crafting bread with a knife did not give bread slices");
        helper.assertTrue(result.getCount() == 2, "expected two bread slices, got " + result.getCount());

        ItemStack remainder = remainderAt(recipe, input, 1);
        helper.assertTrue(remainder.is(KitchenItems.KNIFE.get()), "the knife was consumed instead of being worn");
        helper.assertTrue(remainder.getOrDefault(DataComponents.DAMAGE, 0) == 1, "the knife did not take a point of damage");
        helper.succeed();
    }

    /** On its last point of durability the knife is used up rather than handed back broken. */
    private static void spentKnifeIsNotReturned(GameTestHelper helper) {
        ItemStack knife = new ItemStack(KitchenItems.KNIFE.get());
        knife.set(DataComponents.DAMAGE, knife.getOrDefault(DataComponents.MAX_DAMAGE, 0) - 1);

        CraftingInput input = CraftingInput.of(2, 1, List.of(new ItemStack(Items.BREAD), knife));
        CraftingRecipe recipe = recipeFor(helper, input, "bread and a worn out knife");

        helper.assertTrue(remainderAt(recipe, input, 1).isEmpty(), "a knife on its last use was still handed back");
        helper.succeed();
    }

    private static CraftingRecipe recipeFor(GameTestHelper helper, CraftingInput input, String what) {
        Optional<RecipeHolder<CraftingRecipe>> found = helper.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
        helper.assertTrue(found.isPresent(), "no crafting recipe matched " + what);
        return found.get().value();
    }

    private static ItemStack remainderAt(CraftingRecipe recipe, CraftingInput input, int slot) {
        NonNullList<ItemStack> remaining = recipe.getRemainingItems(input);
        return remaining.get(slot);
    }

    /**
     * JEI finds a plugin by annotation on NeoForge but by entrypoint on Fabric, so the annotation
     * alone leaves the pages missing on one loader only.
     */
    private static void jeiPluginIsRegisteredOnFabric(GameTestHelper helper) {
        com.grim3212.assorted.lib.test.TestSupport.assertJeiPluginIsRegistered(helper, Constants.MOD_ID, "com.grim3212.assorted.kitchen.compat.jei.JEIAssortedKitchen");
        helper.succeed();
    }
}
