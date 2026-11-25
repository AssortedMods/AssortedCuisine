package com.grim3212.assorted.health.gametest;

import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where the leftovers go. The sweets are food, so they should go in a composter. */
final class CompostTests {

    private CompostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("sweets_are_compostable", CompostTests::sweetsAreCompostable);
    }

    private static void sweetsAreCompostable(GameTestHelper helper) {
        helper.assertTrue(composts(helper, HealthItems.SWEETS.get()), "sweets are not compostable");
        helper.assertTrue(composts(helper, HealthItems.POWERED_SWEETS.get()), "powered sweets are not compostable");
        helper.succeed();
    }

    /**
     * Whether a composter takes the item. Fabric keeps the chance in vanilla's table and NeoForge in its data map,
     * and the composter asks whichever its loader has.
     */
    private static boolean composts(GameTestHelper helper, ItemLike item) {
        BlockPos pos = new BlockPos(4, 1, 4);
        helper.setBlock(pos, Blocks.COMPOSTER);
        ItemStack stack = new ItemStack(item);
        ComposterBlock.insertItem(null, helper.getBlockState(pos), helper.getLevel(), stack, helper.absolutePos(pos));
        return stack.isEmpty();
    }
}
