package com.grim3212.assorted.sodas.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Cuisine section, which every part shares; the explicit chapter order keeps
 * the section's order whichever parts are installed.
 */
public class SodasManualProvider extends LibManualProvider {

    public SodasManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        ChapterBuilder soda = this.chapter("soda", 5);

        soda.recipes("carbonated", SodasItems.SODA_BOTTLE.get(), SodasItems.SODA_CO2.get(), SodasItems.SODA_CARBONATED_WATER.get()).every(60)
                .opens(SodasItems.SODA_BOTTLE.get(), SodasItems.SODA_CO2.get(),
                        SodasItems.SODA_CARBONATED_WATER.get());
        soda.recipes("types", SodasItems.SODA_SLURM.get(), SodasItems.SODA_APPLE.get(), SodasItems.SODA_GOLDEN_APPLE.get(), SodasItems.SODA_COCOA.get(), SodasItems.SODA_ROOT_BEER.get(), SodasItems.SODA_DIAMOND.get(), SodasItems.SODA_MUSHROOM.get(), SodasItems.SODA_ORANGE.get(), SodasItems.SODA_CREAM_ORANGE.get(), SodasItems.SODA_SPIKED_ORANGE.get())
                .every(60)
                .opens(SodasItems.SODA_SLURM.get(), SodasItems.SODA_APPLE.get(),
                        SodasItems.SODA_GOLDEN_APPLE.get(), SodasItems.SODA_COCOA.get(),
                        SodasItems.SODA_ROOT_BEER.get(), SodasItems.SODA_DIAMOND.get(),
                        SodasItems.SODA_MUSHROOM.get(), SodasItems.SODA_ORANGE.get(),
                        SodasItems.SODA_CREAM_ORANGE.get(), SodasItems.SODA_SPIKED_ORANGE.get());
    }
}
