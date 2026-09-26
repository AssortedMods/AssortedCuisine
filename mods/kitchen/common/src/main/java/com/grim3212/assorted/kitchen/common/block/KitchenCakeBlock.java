package com.grim3212.assorted.kitchen.common.block;

import net.minecraft.world.level.block.CakeBlock;

/**
 * The pies and the chocolate cake. CakeBlock already does everything these did in 1.12 - the bite
 * counter, the shape that shrinks as it is eaten, the comparator output - so this exists only
 * because vanilla's constructor is protected.
 */
public class KitchenCakeBlock extends CakeBlock {

    public KitchenCakeBlock(Properties props) {
        super(props);
    }
}
