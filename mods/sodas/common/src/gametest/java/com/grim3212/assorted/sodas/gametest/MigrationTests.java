package com.grim3212.assorted.sodas.gametest;

import com.grim3212.assorted.sodas.Family;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** With only the sodas installed, the Assorted Cuisine advancement root still has an icon of its own. */
final class MigrationTests {

    private MigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("advancement_root_falls_back_to_the_orange_soda", MigrationTests::advancementRootFallsBackToTheOrangeSoda);
    }

    // The family's first two icons, the kitchen's cheese and cheese burger, are not installed here.
    private static void advancementRootFallsBackToTheOrangeSoda(GameTestHelper helper) {
        Identifier root = Identifier.fromNamespaceAndPath(Family.ID, "root");
        helper.assertTrue(helper.getLevel().getServer().getAdvancements().get(root).value().display().orElseThrow().getIcon().item().value() == SodasItems.SODA_ORANGE.get(),
                "the Assorted Cuisine advancement root is not drawn with the orange soda, the first of the family's icons installed");
        helper.succeed();
    }
}
