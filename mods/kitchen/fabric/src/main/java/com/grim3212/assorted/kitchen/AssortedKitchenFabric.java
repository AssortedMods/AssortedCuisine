package com.grim3212.assorted.kitchen;

import com.grim3212.assorted.kitchen.common.handlers.KitchenCompostables;
import net.fabricmc.api.ModInitializer;

public class AssortedKitchenFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        KitchenCommonMod.init();

        // Keyed by the items themselves, so it waits until the registries are filled.
        KitchenCompostables.init();
    }
}
