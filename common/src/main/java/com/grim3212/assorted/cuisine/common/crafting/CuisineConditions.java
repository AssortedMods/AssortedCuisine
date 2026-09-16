package com.grim3212.assorted.cuisine.common.crafting;

import com.grim3212.assorted.cuisine.CuisineCommonMod;
import com.grim3212.assorted.lib.platform.Services;

/**
 * Grim Cuisine's six subparts. A disabled part still registers its blocks and items; it loses its
 * recipes, its creative tab entries, its manual chapters and whatever it adds to the world.
 */
public class CuisineConditions {

    public static class Parts {
        public static final String CHOCOLATE = "chocolate";
        public static final String DAIRY = "dairy";
        public static final String DRAGON_FRUIT = "dragonfruit";
        public static final String HEALTH = "health";
        public static final String PIES = "pies";
        public static final String SODA = "soda";

        public static final String[] ALL = {CHOCOLATE, DAIRY, DRAGON_FRUIT, HEALTH, PIES, SODA};
    }

    public static void init() {
        Services.CONDITIONS.registerPartCondition(Parts.CHOCOLATE, () -> CuisineCommonMod.COMMON_CONFIG.chocolateEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.DAIRY, () -> CuisineCommonMod.COMMON_CONFIG.dairyEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.DRAGON_FRUIT, () -> CuisineCommonMod.COMMON_CONFIG.dragonFruitEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.HEALTH, () -> CuisineCommonMod.COMMON_CONFIG.healthEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.PIES, () -> CuisineCommonMod.COMMON_CONFIG.piesEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.SODA, () -> CuisineCommonMod.COMMON_CONFIG.sodaEnabled.get());
    }
}
