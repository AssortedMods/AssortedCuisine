package com.grim3212.assorted.dragonfruit.gametest;

import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
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
        out.accept("dragon_fruit_saturation_follows_its_modifier", FoodValueTests::dragonFruitSaturationFollowsItsModifier);
    }

    private static void dragonFruitSaturationFollowsItsModifier(GameTestHelper helper) {
        expect(helper, DragonFruitItems.DRAGON_FRUIT.get(), 4, 0.3F);
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
