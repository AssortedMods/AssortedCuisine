package com.grim3212.assorted.dragonfruit.gametest;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** The Assorted Cuisine advancement root is drawn with the family's first installed icon, which is this part's own when it is alone. */
final class MigrationTests {

    private MigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("advancement_root_falls_back_to_the_dragon_fruit", MigrationTests::advancementRootFallsBackToTheDragonFruit);
    }

    private static void advancementRootFallsBackToTheDragonFruit(GameTestHelper helper) {
        ResourceLocation root = ResourceLocation.fromNamespaceAndPath(Constants.FAMILY_ID, "root");
        Item drawn = helper.getLevel().getServer().getAdvancements().get(root).value().display().orElseThrow().getIcon().getItem();
        Item expected = Families.icon(Constants.FAMILY_ID).getItem();
        helper.assertTrue(drawn == expected, "the Assorted Cuisine advancement root is drawn with " + drawn + ", not the family's first installed icon " + expected);
        // Beside a part whose icon ranks higher, as in a family's combined run, the root rightly shows that part's.
        helper.assertTrue(Families.members(Constants.FAMILY_ID).size() > 1 || drawn == DragonFruitItems.DRAGON_FRUIT.get(),
                "the Assorted Cuisine advancement root is not drawn with the dragon fruit, the first of the family's icons installed");
        helper.succeed();
    }
}
