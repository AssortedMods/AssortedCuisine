package com.grim3212.assorted.sodas.client.data;

import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

/** Every item of this part is a flat sprite named after its id. */
public class SodasItemModelProvider extends ItemModelProvider {

    public SodasItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Sodas item models";
    }

    @Override
    protected void registerModels() {
        SodasItems.ITEMS.getEntries().stream()
                .map(Supplier::get)
                .forEach(this::basicItem);
    }
}
