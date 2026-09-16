package com.grim3212.assorted.cuisine.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * The knife and the mixer: tools that stay in the crafting grid and wear out a point at a time.
 *
 * <p>Item.Properties#craftRemainder only takes a fixed ItemStackTemplate, which cannot express
 * "the same tool, one point more worn", so the wear rides along as a component patch. Both loaders
 * ask the item per stack; returning null once it is spent is what destroys it.
 */
public class KitchenToolItem extends Item {

    public KitchenToolItem(Properties props) {
        super(props);
    }

    private @Nullable ItemStackTemplate craftingRemainder(ItemInstance stack) {
        int damage = stack.getOrDefault(DataComponents.DAMAGE, 0) + 1;
        int maxDamage = stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);

        if (maxDamage > 0 && damage >= maxDamage) {
            return null;
        }

        return new ItemStackTemplate(stack.typeHolder(), 1, DataComponentPatch.builder().set(DataComponents.DAMAGE, damage).build());
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance stack) {
        return this.craftingRemainder(stack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        return this.craftingRemainder(stack);
    }
}
