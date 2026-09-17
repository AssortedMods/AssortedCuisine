package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.cuisine.gametest.CuisineTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/** Where the food comes from and where the leftovers go. */
final class HarvestTests {

    private HarvestTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("knife_harvests_dragon_fruit", HarvestTests::knifeHarvestsDragonFruit);
        out.accept("bare_hand_leaves_the_cactus_alone", HarvestTests::bareHandLeavesTheCactusAlone);
        out.accept("cuisine_food_is_compostable", HarvestTests::cuisineFoodIsCompostable);
    }

    /** A cactus cut with a knife gives its fruit and stays standing, so it can be farmed. */
    private static void knifeHarvestsDragonFruit(GameTestHelper helper) {
        helper.setBlock(CENTRE, Blocks.CACTUS);
        ServerPlayer player = survivalPlayer(helper, new ItemStack(CuisineItems.KNIFE.get()));

        InteractionResult result = rightClick(player, helper.getLevel(), player.getMainHandItem(), helper.absolutePos(CENTRE));
        helper.assertTrue(result.consumesAction(), "the knife did not cut the cactus");
        helper.assertTrue(countInInventory(player, CuisineItems.DRAGON_FRUIT.get()) == 1, "cutting a cactus gave no dragon fruit");
        helper.assertTrue(droppedCount(helper, CuisineItems.DRAGON_FRUIT.get()) == 0, "the dragon fruit was thrown on the floor");
        helper.assertBlockPresent(Blocks.CACTUS, CENTRE);
        helper.assertTrue(player.getMainHandItem().getDamageValue() == 1, "the knife did not wear from the cut");
        helper.succeed();
    }

    private static void bareHandLeavesTheCactusAlone(GameTestHelper helper) {
        helper.setBlock(CENTRE, Blocks.CACTUS);
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.STICK));

        rightClick(player, helper.getLevel(), player.getMainHandItem(), helper.absolutePos(CENTRE));
        helper.assertTrue(countInInventory(player, CuisineItems.DRAGON_FRUIT.get()) == 0, "a stick harvested dragon fruit");
        helper.succeed();
    }

    /** It is all food, so all of it should go in a composter. */
    private static void cuisineFoodIsCompostable(GameTestHelper helper) {
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(CuisineItems.CHEESE.get()), "cheese is not compostable");
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(CuisineItems.DRAGON_FRUIT.get()), "dragon fruit is not compostable");
        helper.assertTrue(ComposterBlock.COMPOSTABLES.containsKey(CuisineItems.RAW_APPLE_PIE.get()), "an unbaked pie is not compostable");
        helper.succeed();
    }
}
