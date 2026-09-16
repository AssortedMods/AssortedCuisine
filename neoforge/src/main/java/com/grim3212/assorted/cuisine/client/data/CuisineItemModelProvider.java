package com.grim3212.assorted.cuisine.client.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Item models for everything that is not a block item - every food, tool and bottle in the mod.
 * They are all flat sprites, and {@code generateFlatItem} derives the texture from the item id.
 */
public class CuisineItemModelProvider extends ModelProvider {

    public CuisineItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Cuisine item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> !(holder.value() instanceof BlockItem));
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        CuisineBlocks.ITEMS.getEntries().stream()
                .map(Supplier::get)
                .filter(item -> !(item instanceof BlockItem))
                .forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
    }
}
