package com.grim3212.assorted.health.gametest;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** With only the health items installed, the Assorted Cuisine advancement root still has an icon of its own. */
final class MigrationTests {

    private MigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("advancement_root_falls_back_to_the_healthpack", MigrationTests::advancementRootFallsBackToTheHealthpack);
    }

    // The family's first three icons, the kitchen's cheese and cheese burger and the orange soda, are not installed here.
    private static void advancementRootFallsBackToTheHealthpack(GameTestHelper helper) {
        Identifier root = Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "root");
        helper.assertTrue(helper.getLevel().getServer().getAdvancements().get(root).value().display().orElseThrow().getIcon().item().value() == HealthItems.HEALTHPACK.get(),
                "the Assorted Cuisine advancement root is not drawn with the health pack, the first of the family's icons installed");
        helper.succeed();
    }
}
