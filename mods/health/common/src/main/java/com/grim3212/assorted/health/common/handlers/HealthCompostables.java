package com.grim3212.assorted.health.common.handlers;

import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

/**
 * The sweets are food, so they belong in a composter. Called once the registries are filled, from
 * {@code FMLCommonSetupEvent} on NeoForge and the initializer on Fabric, as the map is keyed by the items.
 */
public class HealthCompostables {

    public static void init() {
        // Vanilla's rate for seeds and scraps, as the rest of Assorted Cuisine's food is scaled against.
        add(0.3F, HealthItems.SWEETS.get(), HealthItems.POWERED_SWEETS.get());
    }

    private static void add(float chance, ItemLike... items) {
        for (ItemLike item : items) {
            ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
        }
    }
}
