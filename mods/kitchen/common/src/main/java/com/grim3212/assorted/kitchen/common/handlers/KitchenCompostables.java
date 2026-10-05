package com.grim3212.assorted.kitchen.common.handlers;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

/**
 * Everything here is food, so all of it belongs in a composter. None of it was compostable in 1.12
 * because 1.12 had no composter.
 *
 * <p>Called once the registries are filled - from {@code FMLCommonSetupEvent} on NeoForge and from
 * the initializer on Fabric - because the map is keyed by the items themselves.
 */
public class KitchenCompostables {

    public static void init() {
        // Roughly vanilla's scale: seeds and scraps 0.3, plant matter 0.5, whole foods 0.65,
        // a finished baked good 0.85, and a cake 1.0.
        add(0.3F, KitchenItems.COCOA_DUST.get());

        add(0.5F, KitchenItems.BREAD_SLICE.get(), KitchenItems.DOUGH.get(), KitchenItems.PUMPKIN_SLICE.get(),
                KitchenItems.CHOCOLATE_BALL.get(), KitchenItems.CHOCOLATE_BAR.get(), KitchenItems.CHOCOLATE_BAR_WRAPPED.get());

        add(0.65F, KitchenItems.CHEESE.get(), KitchenItems.BUTTER.get(), KitchenItems.EGGS_COOKED.get(),
                KitchenItems.HOT_CHEESE.get(), KitchenItems.CHEESE_BURGER.get());

        add(0.85F, KitchenItems.RAW_EMPTY_PIE.get(), KitchenItems.RAW_APPLE_PIE.get(), KitchenItems.RAW_MELON_PIE.get(),
                KitchenItems.RAW_PUMPKIN_PIE.get(), KitchenItems.RAW_CHOCOLATE_PIE.get(), KitchenItems.RAW_PORK_PIE.get());

        // The baked pies and the cakes are blocks, and go in at the rate vanilla cake does.
        add(1.0F, KitchenBlocks.APPLE_PIE.get(), KitchenBlocks.MELON_PIE.get(), KitchenBlocks.PUMPKIN_PIE.get(),
                KitchenBlocks.CHOCOLATE_PIE.get(), KitchenBlocks.PORK_PIE.get(), KitchenBlocks.CHOCOLATE_CAKE.get(),
                KitchenBlocks.CHEESE_BLOCK.get());
    }

    private static void add(float chance, ItemLike... items) {
        for (ItemLike item : items) {
            ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
        }
    }
}
