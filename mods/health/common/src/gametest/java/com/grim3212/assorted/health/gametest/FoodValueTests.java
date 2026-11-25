package com.grim3212.assorted.health.gametest;

import com.grim3212.assorted.health.common.item.HealthItems;
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
        out.accept("sweets_saturation_follows_its_modifier", FoodValueTests::sweetsSaturationFollowsItsModifier);
    }

    private static void sweetsSaturationFollowsItsModifier(GameTestHelper helper) {
        expect(helper, HealthItems.SWEETS.get(), 2, 0.1F);
        expect(helper, HealthItems.POWERED_SWEETS.get(), 6, 0.3F);
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
