package com.grim3212.assorted.cuisine.config;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;

import java.util.function.Supplier;

/**
 * 1.12 skipped registering a disabled subpart's blocks and items. A registry that changes between
 * client and server, or between two loads of the same world, breaks, so everything registers and a
 * part only gates its recipes, creative tab entries, manual chapters and world content; see
 * {@code CuisineConditions}.
 */
public class CuisineCommonConfig {

    public final Supplier<Boolean> chocolateEnabled;
    public final Supplier<Boolean> dairyEnabled;
    public final Supplier<Boolean> dragonFruitEnabled;
    public final Supplier<Boolean> healthEnabled;
    public final Supplier<Boolean> piesEnabled;
    public final Supplier<Boolean> sodaEnabled;

    public final Supplier<Double> dragonFruitChance;

    public CuisineCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        chocolateEnabled = builder.defineBoolean("parts.chocolateEnabled", true, "Set this to true if you would like the chocolate items to be craftable and found in the creative tab.");
        dairyEnabled = builder.defineBoolean("parts.dairyEnabled", true, "Set this to true if you would like the butter churn, cheese maker, cheese, sandwiches and eggs to be craftable and found in the creative tab.");
        dragonFruitEnabled = builder.defineBoolean("parts.dragonFruitEnabled", true, "Set this to true if you would like cacti to drop dragon fruit and for it to be found in the creative tab.");
        healthEnabled = builder.defineBoolean("parts.healthEnabled", true, "Set this to true if you would like the sweets, bandages and health packs to be craftable and found in the creative tab.");
        piesEnabled = builder.defineBoolean("parts.piesEnabled", true, "Set this to true if you would like pies to be craftable and found in the creative tab.");
        sodaEnabled = builder.defineBoolean("parts.sodaEnabled", true, "Set this to true if you would like sodas to be craftable and found in the creative tab.");

        dragonFruitChance = builder.defineDouble("dragonFruit.dropChance", 0.33D, 0, 1, "The chance that breaking a cactus also drops dragon fruit. Set to 0 to turn the drop off.");

        builder.setup();
    }
}
