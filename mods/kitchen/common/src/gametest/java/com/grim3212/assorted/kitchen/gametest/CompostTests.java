package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where the leftovers go. It is all food, so all of it should go in a composter. */
final class CompostTests {

    private CompostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("cuisine_food_is_compostable", CompostTests::cuisineFoodIsCompostable);
    }

    private static void cuisineFoodIsCompostable(GameTestHelper helper) {
        helper.assertTrue(new ItemStack(KitchenItems.CHEESE.get()).has(DataComponents.COMPOSTABLE), "cheese is not compostable");
        helper.assertTrue(new ItemStack(KitchenItems.RAW_APPLE_PIE.get()).has(DataComponents.COMPOSTABLE), "an unbaked pie is not compostable");
        helper.succeed();
    }
}
