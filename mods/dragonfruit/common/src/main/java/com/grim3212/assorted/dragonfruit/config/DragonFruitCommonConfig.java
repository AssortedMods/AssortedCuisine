package com.grim3212.assorted.dragonfruit.config;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;

import java.util.function.Supplier;

public class DragonFruitCommonConfig {

    public final Supplier<Double> dragonFruitChance;

    public DragonFruitCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        dragonFruitChance = builder.defineDouble("dragonFruit.dropChance", 0.33D, 0, 1, "The chance that breaking a cactus also drops dragon fruit. Set to 0 to turn the drop off.");

        builder.setup();
    }
}
