package com.grim3212.assorted.sodas.client.data;

import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Item models for every bottle in the mod. They are all flat sprites, and {@code generateFlatItem}
 * derives the texture from the item id.
 */
public class SodasItemModelProvider extends ModelProvider {

    public SodasItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Sodas item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        SodasItems.ITEMS.getEntries().stream()
                .map(Supplier::get)
                .forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
    }
}
