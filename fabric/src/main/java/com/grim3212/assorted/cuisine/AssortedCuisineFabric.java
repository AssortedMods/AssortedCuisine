package com.grim3212.assorted.cuisine;

import com.grim3212.assorted.cuisine.common.handlers.CuisineCompostables;
import net.fabricmc.api.ModInitializer;

public class AssortedCuisineFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CuisineCommonMod.init();

        // Keyed by the items themselves, so it waits until the registries are filled.
        CuisineCompostables.init();
    }
}
