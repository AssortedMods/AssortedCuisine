package com.grim3212.assorted.cuisine.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Cuisine. The tests live in small {@code <Feature>Tests}
 * classes; this only lists them. Each name here needs a matching
 * {@code data/assortedcuisine/test_instance/<name>.json}.
 */
public final class CuisineGameTests {

    private CuisineGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        SmokeTests.register(out);
        MachineTests.register(out);
        DrinkTests.register(out);
        CompostTests.register(out);
        AutomationTests.register(out);
        RecipeTests.register(out);
        PartTests.register(out);
    }
}
