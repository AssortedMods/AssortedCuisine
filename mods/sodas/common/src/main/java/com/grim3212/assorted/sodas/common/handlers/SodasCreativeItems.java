package com.grim3212.assorted.sodas.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.sodas.Family;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/** This part's share of the Assorted Cuisine tab, which every part asks for and the first to load registers. */
public class SodasCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        // After the kitchen, health and dragon fruit, as the tab was when this was all one mod.
        SharedCreativeTabs.add(TAB, 400, SodasCreativeItems::items);
    }

    private static List<ItemStack> items() {
        return stacks(SodasItems.SODA_BOTTLE, SodasItems.SODA_CO2, SodasItems.SODA_CARBONATED_WATER, SodasItems.SODA_APPLE,
                SodasItems.SODA_ORANGE, SodasItems.SODA_CREAM_ORANGE, SodasItems.SODA_SPIKED_ORANGE, SodasItems.SODA_ROOT_BEER,
                SodasItems.SODA_COCOA, SodasItems.SODA_GOLDEN_APPLE, SodasItems.SODA_DIAMOND, SodasItems.SODA_MUSHROOM,
                SodasItems.SODA_SLURM);
    }

    @SafeVarargs
    private static List<ItemStack> stacks(IRegistryObject<Item>... items) {
        return Arrays.stream(items).map(item -> new ItemStack(item.get())).toList();
    }
}
