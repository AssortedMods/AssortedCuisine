package com.grim3212.assorted.health.gametest;

import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where the leftovers go. The sweets are food, so they should go in a composter. */
final class CompostTests {

    private CompostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("sweets_are_compostable", CompostTests::sweetsAreCompostable);
    }

    private static void sweetsAreCompostable(GameTestHelper helper) {
        helper.assertTrue(new ItemStack(HealthItems.SWEETS.get()).has(DataComponents.COMPOSTABLE), "sweets are not compostable");
        helper.assertTrue(new ItemStack(HealthItems.POWERED_SWEETS.get()).has(DataComponents.COMPOSTABLE), "powered sweets are not compostable");
        helper.succeed();
    }
}
