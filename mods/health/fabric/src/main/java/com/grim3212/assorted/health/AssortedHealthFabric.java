package com.grim3212.assorted.health;

import com.grim3212.assorted.health.common.handlers.HealthCompostables;
import net.fabricmc.api.ModInitializer;

public class AssortedHealthFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        HealthCommonMod.init();

        // Keyed by the items themselves, so it waits until the registries are filled.
        HealthCompostables.init();
    }
}
