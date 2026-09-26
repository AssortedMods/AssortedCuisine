package com.grim3212.assorted.health.client.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.common.item.HealthItems;
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
 * Item models for every sweet and health pack in the mod. They are all flat sprites, and
 * {@code generateFlatItem} derives the texture from the item id.
 */
public class HealthItemModelProvider extends ModelProvider {

    public HealthItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Health item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        HealthItems.ITEMS.getEntries().stream()
                .map(Supplier::get)
                .forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
    }
}
