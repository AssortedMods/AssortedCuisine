package com.grim3212.assorted.dragonfruit.gametest;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** With only the dragon fruit installed, the Assorted Cuisine advancement root still has an icon of its own. */
final class MigrationTests {

    private MigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("advancement_root_falls_back_to_the_dragon_fruit", MigrationTests::advancementRootFallsBackToTheDragonFruit);
    }

    // Every icon of the family before the dragon fruit, the last of them, belongs to a part not installed here.
    private static void advancementRootFallsBackToTheDragonFruit(GameTestHelper helper) {
        Identifier root = Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "root");
        helper.assertTrue(helper.getLevel().getServer().getAdvancements().get(root).value().display().orElseThrow().getIcon().item().value() == DragonFruitItems.DRAGON_FRUIT.get(),
                "the Assorted Cuisine advancement root is not drawn with the dragon fruit, the first of the family's icons installed");
        helper.succeed();
    }
}
