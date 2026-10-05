package com.grim3212.assorted.dragonfruit.client.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/** The dragon fruit's item model, a flat sprite whose texture {@code generateFlatItem} derives from the item id. */
public class DragonFruitItemModelProvider extends ModelProvider {

    public DragonFruitItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Dragon Fruit item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(DragonFruitItems.DRAGON_FRUIT.get(), ModelTemplates.FLAT_ITEM);
    }
}
