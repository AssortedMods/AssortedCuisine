package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.ComposterBlock;

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
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(CuisineItems.CHEESE.get()), "cheese is not compostable");
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(CuisineItems.DRAGON_FRUIT.get()), "dragon fruit is not compostable");
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(CuisineItems.RAW_APPLE_PIE.get()), "an unbaked pie is not compostable");
        helper.succeed();
    }
}
