package com.grim3212.assorted.cuisine.common.item;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import net.minecraft.world.item.BlockItem;

/**
 * Cocoa fruit: plant it as a cocoa sapling. A BlockItem placing the sapling gives the placement
 * rules, the sounds and the "can I build here" checks for free, which the 1.12 item reimplemented
 * by hand.
 *
 * <p>The properties deliberately do not call useBlockDescriptionPrefix(), so this is named as
 * cocoa fruit rather than after the sapling block it places.
 */
public class CocoaFruitItem extends BlockItem {

    public CocoaFruitItem(Properties props) {
        super(CuisineBlocks.COCOA_SAPLING.get(), props);
    }
}
