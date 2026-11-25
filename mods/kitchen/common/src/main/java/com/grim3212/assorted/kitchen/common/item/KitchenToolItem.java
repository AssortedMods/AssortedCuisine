package com.grim3212.assorted.kitchen.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * The knife, the whisk and the mortar and pestle: tools that stay in the crafting grid and wear out a
 * point at a time.
 */
public class KitchenToolItem extends Item {

    public KitchenToolItem(Properties props) {
        super(props);
    }

    /** The tool a point more worn, or nothing once that point would break it. */
    private ItemStack craftingRemainder(ItemStack stack) {
        int damage = stack.getDamageValue() + 1;
        if (stack.getMaxDamage() > 0 && damage >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }

        ItemStack worn = stack.copyWithCount(1);
        worn.setDamageValue(damage);
        return worn;
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return this.craftingRemainder(stack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return this.craftingRemainder(stack);
    }
}
