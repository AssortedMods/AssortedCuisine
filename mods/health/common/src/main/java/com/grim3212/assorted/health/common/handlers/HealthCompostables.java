package com.grim3212.assorted.health.common.handlers;

import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.crafting.CompostChances;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * The sweets are food, so they belong in a composter. Called from common init: Fabric registers the chance as
 * the mod loads, and NeoForge datagen writes it into the {@code neoforge:compostables} data map.
 */
public class HealthCompostables {

    public static void init() {
        // Vanilla's rate for seeds and scraps, as the rest of Assorted Cuisine's food is scaled against.
        add(0.3F, HealthItems.SWEETS, HealthItems.POWERED_SWEETS);
    }

    @SafeVarargs
    private static void add(float chance, Supplier<? extends ItemLike>... items) {
        for (Supplier<? extends ItemLike> item : items) {
            CompostChances.add(item, chance);
        }
    }
}
