package com.grim3212.assorted.kitchen.client.data;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

/**
 * Item models for everything that is not a block item - every food, tool and bottle in the mod.
 * They are all flat sprites, and {@code basicItem} derives the texture from the item id. The block
 * items are {@link KitchenBlockstateProvider}'s.
 */
public class KitchenItemModelProvider extends ItemModelProvider {

    public KitchenItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Kitchen item models";
    }

    @Override
    protected void registerModels() {
        KitchenBlocks.ITEMS.getEntries().stream()
                .map(Supplier::get)
                .filter(item -> !(item instanceof BlockItem))
                .forEach(this::basicItem);
    }
}
