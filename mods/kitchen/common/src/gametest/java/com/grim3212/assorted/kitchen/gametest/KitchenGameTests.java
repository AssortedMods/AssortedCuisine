package com.grim3212.assorted.kitchen.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Kitchen. The tests live in small {@code <Feature>Tests} classes;
 * this only lists them. Each name needs a matching {@code data/assortedkitchen/test_instance/<name>.json}.
 */
public final class KitchenGameTests {

    private KitchenGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        SmokeTests.register(out);
        MachineTests.register(out);
        CompostTests.register(out);
        AutomationTests.register(out);
        RecipeTests.register(out);
        AliasTests.register(out);
        MigrationTests.register(out);
        FamilyTests.register(out);
    }
}
