package com.grim3212.assorted.dragonfruit.gametest;

import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where the leftovers go. Dragon fruit is food, so it should go in a composter. */
final class CompostTests {

    private CompostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("dragon_fruit_is_compostable", CompostTests::dragonFruitIsCompostable);
    }

    private static void dragonFruitIsCompostable(GameTestHelper helper) {
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(DragonFruitItems.DRAGON_FRUIT.get()), "dragon fruit is not compostable");
        helper.succeed();
    }
}
