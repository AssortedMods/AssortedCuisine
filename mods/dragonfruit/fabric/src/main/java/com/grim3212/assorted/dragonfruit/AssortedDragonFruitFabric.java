package com.grim3212.assorted.dragonfruit;

import com.grim3212.assorted.dragonfruit.common.handlers.DragonFruitCompostables;
import net.fabricmc.api.ModInitializer;

public class AssortedDragonFruitFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DragonFruitCommonMod.init();

        // Keyed by the items themselves, so it waits until the registries are filled.
        DragonFruitCompostables.init();
    }
}
