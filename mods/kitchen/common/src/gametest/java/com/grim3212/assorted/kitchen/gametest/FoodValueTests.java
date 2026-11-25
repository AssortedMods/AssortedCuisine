package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** What each food is worth. Saturation is a modifier, as it is for vanilla food: eating restores nutrition x modifier x 2. */
final class FoodValueTests {

    private FoodValueTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("kitchen_food_saturation_follows_its_modifier", FoodValueTests::kitchenFoodSaturationFollowsItsModifier);
    }

    private static void kitchenFoodSaturationFollowsItsModifier(GameTestHelper helper) {
        expect(helper, KitchenItems.BUTTER.get(), 2, 0.4F);
        expect(helper, KitchenItems.CHEESE.get(), 3, 0.6F);
        expect(helper, KitchenItems.BREAD_SLICE.get(), 2, 0.4F);
        expect(helper, KitchenItems.CHEESE_BURGER.get(), 12, 0.95F);
        expect(helper, KitchenItems.HOT_CHEESE.get(), 8, 0.75F);
        expect(helper, KitchenItems.EGGS_UNMIXED.get(), 2, 0.1F);
        expect(helper, KitchenItems.EGGS_MIXED.get(), 4, 0.4F);
        expect(helper, KitchenItems.EGGS_COOKED.get(), 10, 0.8F);
        expect(helper, KitchenItems.CHOCOLATE_BOWL.get(), 4, 0.3F);
        expect(helper, KitchenItems.HOT_CHOCOLATE.get(), 6, 0.6F);
        expect(helper, KitchenItems.CHOCOLATE_BALL.get(), 2, 0.2F);
        expect(helper, KitchenItems.CHOCOLATE_BAR.get(), 3, 0.8F);
        expect(helper, KitchenItems.CHOCOLATE_BAR_WRAPPED.get(), 5, 0.8F);
        expect(helper, KitchenItems.DOUGH.get(), 1, 0.2F);
        expect(helper, KitchenItems.PUMPKIN_SLICE.get(), 1, 0.2F);
        expect(helper, KitchenItems.RAW_APPLE_PIE.get(), 2, 0.3F);
        helper.succeed();
    }

    private static void expect(GameTestHelper helper, Item item, int nutrition, float saturationModifier) {
        FoodProperties food = new ItemStack(item).get(DataComponents.FOOD);
        helper.assertTrue(food != null, item + " is not food");
        helper.assertValueEqual(food.nutrition(), nutrition, "the hunger " + item + " restores");
        float saturation = nutrition * saturationModifier * 2.0F;
        helper.assertTrue(Math.abs(food.saturation() - saturation) < 0.001F, item + " restores " + food.saturation() + " saturation, not the " + saturation + " its modifier of " + saturationModifier + " gives");
    }
}
