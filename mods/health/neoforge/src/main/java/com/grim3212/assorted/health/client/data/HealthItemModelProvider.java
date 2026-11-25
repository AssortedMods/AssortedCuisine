package com.grim3212.assorted.health.client.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

/** Every item of this part is a flat sprite named after its id. */
public class HealthItemModelProvider extends ItemModelProvider {

    public HealthItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Health item models";
    }

    @Override
    protected void registerModels() {
        HealthItems.ITEMS.getEntries().stream()
                .map(Supplier::get)
                .forEach(this::basicItem);
    }
}
