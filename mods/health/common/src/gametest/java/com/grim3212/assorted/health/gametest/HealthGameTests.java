package com.grim3212.assorted.health.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Health. The tests live in small {@code <Feature>Tests} classes;
 * this only lists them. Each name needs a matching {@code data/assortedhealth/test_instance/<name>.json}.
 */
public final class HealthGameTests {

    private HealthGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        SmokeTests.register(out);
        AliasTests.register(out);
        MigrationTests.register(out);
        HealingTests.register(out);
        CompostTests.register(out);
        FamilyTests.register(out);
    }
}
