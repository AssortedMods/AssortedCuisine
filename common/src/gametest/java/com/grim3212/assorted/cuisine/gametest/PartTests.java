package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.crafting.CuisineConditions.Parts;
import com.grim3212.assorted.lib.conditions.LibParts;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The Grim Cuisine subparts. Recipes, creative tab and manual all name a part by string, and an
 * unregistered one reads as enabled, so a missed registration would never show up as missing content.
 */
final class PartTests {

    private PartTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("parts_registered_and_enabled", PartTests::partsRegisteredAndEnabled);
    }

    private static void partsRegisteredAndEnabled(GameTestHelper helper) {
        for (String part : Parts.ALL) {
            helper.assertTrue(LibParts.isRegistered(part), "part " + part + " was never registered");
            helper.assertTrue(LibParts.isEnabled(part), "part " + part + " is off in the default config");
        }

        // Gated on two parts at once, so it only loads if the combined condition passes.
        ResourceKey<Recipe<?>> chocolatePie = ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "raw_chocolate_pie"));
        helper.assertTrue(helper.getLevel().getServer().getRecipeManager().byKey(chocolatePie).isPresent(), "the raw chocolate pie recipe did not load with every part on");
        helper.succeed();
    }
}
