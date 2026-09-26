package com.grim3212.assorted.dragonfruit.common.handlers;

import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.world.level.block.ComposterBlock;

/**
 * Dragon fruit is food, so it belongs in a composter. Called once the registries are filled, from
 * {@code FMLCommonSetupEvent} on NeoForge and the initializer on Fabric, as the map is keyed by the items.
 */
public class DragonFruitCompostables {

    public static void init() {
        // Vanilla's rate for a whole food, the way it takes an apple.
        ComposterBlock.COMPOSTABLES.put(DragonFruitItems.DRAGON_FRUIT.get(), 0.65F);
    }
}
