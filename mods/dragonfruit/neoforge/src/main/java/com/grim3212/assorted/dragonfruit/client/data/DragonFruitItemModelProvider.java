package com.grim3212.assorted.dragonfruit.client.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** The dragon fruit's item model, a flat sprite whose texture {@code basicItem} derives from the item id. */
public class DragonFruitItemModelProvider extends ItemModelProvider {

    public DragonFruitItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Dragon Fruit item models";
    }

    @Override
    protected void registerModels() {
        this.basicItem(DragonFruitItems.DRAGON_FRUIT.get());
    }
}
