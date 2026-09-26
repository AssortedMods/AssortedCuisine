package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.Family;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** With the kitchen installed, the Assorted Cuisine advancement root is drawn with its cheese. */
final class MigrationTests {

    private MigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("advancement_root_is_drawn_with_the_cheese", MigrationTests::advancementRootIsDrawnWithTheCheese);
    }

    // Cheese is the first of the family's icons, so it wins over the placeholder whichever part's root loaded.
    private static void advancementRootIsDrawnWithTheCheese(GameTestHelper helper) {
        Identifier root = Identifier.fromNamespaceAndPath(Family.ID, "root");
        helper.assertTrue(helper.getLevel().getServer().getAdvancements().get(root).value().display().orElseThrow().getIcon().item().value() == KitchenItems.CHEESE.get(),
                "the Assorted Cuisine advancement root is not drawn with the cheese, the first of the family's icons");
        helper.succeed();
    }
}
