package com.grim3212.assorted.dragonfruit.gametest;

import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where the leftovers go. Dragon fruit is food, so it should go in a composter. */
final class CompostTests {

    private CompostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("dragon_fruit_is_compostable", CompostTests::dragonFruitIsCompostable);
    }

    private static void dragonFruitIsCompostable(GameTestHelper helper) {
        // Fabric keeps the chance in vanilla's table and NeoForge in its data map; the composter asks whichever its loader has.
        BlockPos pos = new BlockPos(4, 1, 4);
        helper.setBlock(pos, Blocks.COMPOSTER);
        ItemStack fruit = new ItemStack(DragonFruitItems.DRAGON_FRUIT.get());
        ComposterBlock.insertItem(null, helper.getBlockState(pos), helper.getLevel(), fruit, helper.absolutePos(pos));
        helper.assertTrue(fruit.isEmpty(), "dragon fruit is not compostable");
        helper.succeed();
    }
}
