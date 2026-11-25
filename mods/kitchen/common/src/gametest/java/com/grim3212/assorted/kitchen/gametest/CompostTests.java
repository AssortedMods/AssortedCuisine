package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where the leftovers go. It is all food, so all of it should go in a composter. */
final class CompostTests {

    private CompostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("cuisine_food_is_compostable", CompostTests::cuisineFoodIsCompostable);
    }

    private static void cuisineFoodIsCompostable(GameTestHelper helper) {
        helper.assertTrue(composts(helper, KitchenItems.CHEESE.get()), "cheese is not compostable");
        helper.assertTrue(composts(helper, KitchenItems.RAW_APPLE_PIE.get()), "an unbaked pie is not compostable");
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
