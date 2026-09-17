package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.block.CuisineMachineBlock;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineBlockEntityTypes;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.network.chat.Component;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
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
     * Runs a machine's block entity ticker until something is waiting in its output or
     * {@code maxTicks} pass, the way the level would. Ticking it by hand keeps the test synchronous.
     */
    static void runMachine(GameTestHelper helper, BlockPos rel, int maxTicks) {
        BlockPos pos = helper.absolutePos(rel);
        ServerLevel level = helper.getLevel();
        CuisineMachineBlock block = (CuisineMachineBlock) helper.getBlockState(rel).getBlock();
        BlockEntityTicker<CuisineMachineBlockEntity> ticker = block.getTicker(level, helper.getBlockState(rel), CuisineBlockEntityTypes.MACHINE.get());
        helper.assertTrue(ticker != null, "the machine has no server ticker");

        for (int i = 0; i < maxTicks; i++) {
            CuisineMachineBlockEntity entity = machine(helper, rel);
            if (!entity.getItem(CuisineMachineBlockEntity.SLOT_OUTPUT).isEmpty()) {
                return;
            }

            // The state is re-read each time: finishing a stage replaces the blockstate.
            ticker.tick(level, pos, level.getBlockState(pos), entity);
        }
    }

    static CuisineMachineBlockEntity machine(GameTestHelper helper, BlockPos rel) {
        BlockEntity entity = helper.getLevel().getBlockEntity(helper.absolutePos(rel));

        if (!(entity instanceof CuisineMachineBlockEntity machine)) {
            throw new GameTestAssertException(Component.literal("no machine block entity at " + rel), 0);
        }

        return machine;
    }

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
