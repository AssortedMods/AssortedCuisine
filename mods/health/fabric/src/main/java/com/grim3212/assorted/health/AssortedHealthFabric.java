package com.grim3212.assorted.health;

import net.fabricmc.api.ModInitializer;

public class AssortedHealthFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        HealthCommonMod.init();
    }
}
