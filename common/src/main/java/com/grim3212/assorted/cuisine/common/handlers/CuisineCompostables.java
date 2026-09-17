package com.grim3212.assorted.cuisine.common.handlers;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

/**
 * Everything here is food, so all of it belongs in a composter. None of it was compostable in 1.12
 * because 1.12 had no composter.
 *
 * <p>Called once the registries are filled - from {@code FMLCommonSetupEvent} on NeoForge and from
 * the initializer on Fabric - because the map is keyed by the items themselves.
 */
public class CuisineCompostables {

    public static void init() {
        // Roughly vanilla's scale: seeds and scraps 0.3, plant matter 0.5, whole foods 0.65,
        // a finished baked good 0.85, and a cake 1.0.
        add(0.3F, CuisineItems.COCOA_DUST.get(), CuisineItems.SWEETS.get(), CuisineItems.POWERED_SWEETS.get());

        add(0.5F, CuisineItems.BREAD_SLICE.get(), CuisineItems.DOUGH.get(), CuisineItems.PUMPKIN_SLICE.get(),
                CuisineItems.CHOCOLATE_BALL.get(), CuisineItems.CHOCOLATE_BAR.get(), CuisineItems.CHOCOLATE_BAR_WRAPPED.get());

        add(0.65F, CuisineItems.CHEESE.get(), CuisineItems.BUTTER.get(), CuisineItems.DRAGON_FRUIT.get(),
                CuisineItems.EGGS_COOKED.get(), CuisineItems.HOT_CHEESE.get(), CuisineItems.CHEESE_BURGER.get());

        add(0.85F, CuisineItems.RAW_EMPTY_PIE.get(), CuisineItems.RAW_APPLE_PIE.get(), CuisineItems.RAW_MELON_PIE.get(),
                CuisineItems.RAW_PUMPKIN_PIE.get(), CuisineItems.RAW_CHOCOLATE_PIE.get(), CuisineItems.RAW_PORK_PIE.get());

        // The baked pies and the cakes are blocks, and go in at the rate vanilla cake does.
        add(1.0F, CuisineBlocks.APPLE_PIE.get(), CuisineBlocks.MELON_PIE.get(), CuisineBlocks.PUMPKIN_PIE.get(),
                CuisineBlocks.CHOCOLATE_PIE.get(), CuisineBlocks.PORK_PIE.get(), CuisineBlocks.CHOCOLATE_CAKE.get(),
                CuisineBlocks.CHEESE_BLOCK.get());
    }

    private static void add(float chance, ItemLike... items) {
        for (ItemLike item : items) {
            ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
        }
    }
}
