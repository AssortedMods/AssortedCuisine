package com.grim3212.assorted.kitchen.common.handlers;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Cuisine tab, which every part asks for and the first to load registers. */
public class KitchenCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    private static List<ItemStack> getCreativeItems() {
        CreativeTabItems items = new CreativeTabItems();

        items.add(KitchenBlocks.BUTTER_CHURN.get());
        items.add(KitchenBlocks.CHEESE_MAKER.get());
        items.add(KitchenBlocks.CHEESE_BLOCK.get());
        items.add(KitchenItems.BUTTER.get());
        items.add(KitchenItems.CHEESE.get());
        items.add(KitchenItems.KNIFE.get());
        items.add(KitchenItems.WHISK.get());
        items.add(KitchenItems.BREAD_SLICE.get());
        items.add(KitchenItems.CHEESE_BURGER.get());
        items.add(KitchenItems.HOT_CHEESE.get());
        items.add(KitchenItems.EGGS_UNMIXED.get());
        items.add(KitchenItems.EGGS_MIXED.get());
        items.add(KitchenItems.EGGS_COOKED.get());

        items.add(KitchenItems.MORTAR_AND_PESTLE.get());
        items.add(KitchenItems.COCOA_DUST.get());
        items.add(KitchenItems.CHOCOLATE_BOWL.get());
        items.add(KitchenItems.HOT_CHOCOLATE.get());
        items.add(KitchenBlocks.CHOCOLATE_BAR_MOULD.get());
        items.add(KitchenItems.CHOCOLATE_BAR.get());
        items.add(KitchenItems.WRAPPER.get());
        items.add(KitchenItems.CHOCOLATE_BAR_WRAPPED.get());
        items.add(KitchenItems.CHOCOLATE_BALL.get());
        items.add(KitchenBlocks.CHOCOLATE_BLOCK.get());
        items.add(KitchenBlocks.CHOCOLATE_CAKE.get());

        items.add(KitchenItems.PAN.get());
        items.add(KitchenItems.DOUGH.get());
        items.add(KitchenItems.PUMPKIN_SLICE.get());
        items.add(KitchenItems.RAW_EMPTY_PIE.get());
        items.add(KitchenItems.RAW_APPLE_PIE.get());
        items.add(KitchenItems.RAW_MELON_PIE.get());
        items.add(KitchenItems.RAW_PUMPKIN_PIE.get());
        items.add(KitchenItems.RAW_CHOCOLATE_PIE.get());
        items.add(KitchenItems.RAW_PORK_PIE.get());
        items.add(KitchenBlocks.APPLE_PIE.get());
        items.add(KitchenBlocks.MELON_PIE.get());
        items.add(KitchenBlocks.PUMPKIN_PIE.get());
        items.add(KitchenBlocks.CHOCOLATE_PIE.get());
        items.add(KitchenBlocks.PORK_PIE.get());

        return items.getItems();
    }

    public static void init() {
        // First in the tab, as dairy, chocolate and pies were when this was all one mod.
        SharedCreativeTabs.add(TAB, 100, KitchenCreativeItems::getCreativeItems);
    }
}
