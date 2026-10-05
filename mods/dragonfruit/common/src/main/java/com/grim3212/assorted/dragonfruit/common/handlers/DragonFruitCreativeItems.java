package com.grim3212.assorted.dragonfruit.common.handlers;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Cuisine tab, which every part asks for and the first to load registers. */
public class DragonFruitCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        // After the kitchen and health and before the sodas, as the tab was when this was all one mod.
        SharedCreativeTabs.add(TAB, 300, () -> List.of(new ItemStack(DragonFruitItems.DRAGON_FRUIT.get())));
    }
}
