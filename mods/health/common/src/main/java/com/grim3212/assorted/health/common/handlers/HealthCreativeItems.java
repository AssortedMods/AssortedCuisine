package com.grim3212.assorted.health.common.handlers;

import com.grim3212.assorted.health.Family;
import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/** This part's share of the Assorted Cuisine tab, which every part asks for and the first to load registers. */
public class HealthCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        // After the kitchen and before the dragon fruit and sodas, as the tab was when this was all one mod.
        SharedCreativeTabs.add(TAB, 200, HealthCreativeItems::items);
    }

    private static List<ItemStack> items() {
        return stacks(HealthItems.SWEETS, HealthItems.POWERED_SUGAR, HealthItems.POWERED_SWEETS, HealthItems.BANDAGE,
                HealthItems.HEALTHPACK, HealthItems.HEALTHPACK_SUPER);
    }

    @SafeVarargs
    private static List<ItemStack> stacks(IRegistryObject<Item>... items) {
        return Arrays.stream(items).map(item -> new ItemStack(item.get())).toList();
    }
}
