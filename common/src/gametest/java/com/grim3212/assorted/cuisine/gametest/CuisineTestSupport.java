package com.grim3212.assorted.cuisine.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Helpers and constants shared by Assorted Cuisine's gametest classes, which import them statically,
 * alongside AssortedLib's {@code TestSupport}.
 */
final class CuisineTestSupport {

    private CuisineTestSupport() {
    }

    /** Middle of the 9x9x9 box, one block above its floor - room on every side for a drop. */
    static final BlockPos CENTRE = new BlockPos(4, 1, 4);

    /**
     * How many of {@code item} are lying on the floor of the test box. The machines here hand their
     * output back as a dropped entity rather than into the inventory, so this is what they assert on.
     */
    static int droppedCount(GameTestHelper helper, Item item) {
        AABB box = AABB.encapsulatingFullBlocks(helper.absolutePos(BlockPos.ZERO), helper.absolutePos(new BlockPos(9, 9, 9)));
        List<ItemEntity> entities = helper.getLevel().getEntitiesOfClass(ItemEntity.class, box);

        int count = 0;
        for (ItemEntity entity : entities) {
            if (entity.getItem().is(item)) {
                count += entity.getItem().getCount();
            }
        }
        return count;
    }
}
