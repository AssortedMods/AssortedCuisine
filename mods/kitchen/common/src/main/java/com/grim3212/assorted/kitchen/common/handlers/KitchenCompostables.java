package com.grim3212.assorted.kitchen.common.handlers;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.lib.crafting.CompostChances;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * Everything here is food, so all of it belongs in a composter. Called from common init: the chance is a component on
 * the item, set as the item is made.
 */
public class KitchenCompostables {

    public static void init() {
        // Roughly vanilla's scale: seeds and scraps 0.3, plant matter 0.5, whole foods 0.65,
        // a finished baked good 0.85, and a cake 1.0.
        add(0.3F, KitchenItems.COCOA_DUST);

        add(0.5F, KitchenItems.BREAD_SLICE, KitchenItems.DOUGH, KitchenItems.PUMPKIN_SLICE,
                KitchenItems.CHOCOLATE_BALL, KitchenItems.CHOCOLATE_BAR, KitchenItems.CHOCOLATE_BAR_WRAPPED);

        add(0.65F, KitchenItems.CHEESE, KitchenItems.BUTTER, KitchenItems.EGGS_COOKED,
                KitchenItems.HOT_CHEESE, KitchenItems.CHEESE_BURGER);

        add(0.85F, KitchenItems.RAW_EMPTY_PIE, KitchenItems.RAW_APPLE_PIE, KitchenItems.RAW_MELON_PIE,
                KitchenItems.RAW_PUMPKIN_PIE, KitchenItems.RAW_CHOCOLATE_PIE, KitchenItems.RAW_PORK_PIE);

        // The baked pies and the cakes are blocks, and go in at the rate vanilla cake does.
        add(1.0F, KitchenBlocks.APPLE_PIE, KitchenBlocks.MELON_PIE, KitchenBlocks.PUMPKIN_PIE,
                KitchenBlocks.CHOCOLATE_PIE, KitchenBlocks.PORK_PIE, KitchenBlocks.CHOCOLATE_CAKE,
                KitchenBlocks.CHEESE_BLOCK);
    }

    @SafeVarargs
    private static void add(float chance, Supplier<? extends ItemLike>... items) {
        for (Supplier<? extends ItemLike> item : items) {
            CompostChances.add(item, chance);
        }
    }
}
