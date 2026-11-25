package com.grim3212.assorted.dragonfruit.common.handlers;

import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.crafting.CompostChances;

/**
 * Dragon fruit is food, so it belongs in a composter. Called from common init: Fabric registers the chance as
 * the mod loads, and NeoForge datagen writes it into the {@code neoforge:compostables} data map.
 */
public class DragonFruitCompostables {

    public static void init() {
        // Vanilla's rate for a whole food, the way it takes an apple.
        CompostChances.add(DragonFruitItems.DRAGON_FRUIT, 0.65F);
    }
}
