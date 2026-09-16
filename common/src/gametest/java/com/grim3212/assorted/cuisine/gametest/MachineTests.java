package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.block.ButterChurnBlock;
import com.grim3212.assorted.cuisine.common.block.CheeseMakerBlock;
import com.grim3212.assorted.cuisine.common.block.ChocolateBarMouldBlock;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.cuisine.gametest.CuisineTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * The three blocks that turn one thing into another over time: milk into cheese, milk into butter,
 * hot chocolate into bars. Each is filled by right clicking, advanced by random ticks and emptied by
 * punching, and none of them has a block entity.
 */
final class MachineTests {

    private MachineTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("cheese_maker_makes_cheese", MachineTests::cheeseMakerMakesCheese);
        out.accept("cheese_maker_ignores_empty_hand", MachineTests::cheeseMakerIgnoresEmptyHand);
        out.accept("butter_churn_makes_butter", MachineTests::butterChurnMakesButter);
        out.accept("chocolate_mould_makes_bars", MachineTests::chocolateMouldMakesBars);
    }

    private static void cheeseMakerMakesCheese(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));
        BlockPos pos = helper.absolutePos(CENTRE);

        InteractionResult result = rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        helper.assertTrue(result.consumesAction(), "the milk bucket was not accepted");
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(CheeseMakerBlock.STAGE) > 0, "the cheese maker did not start");
        helper.assertTrue(countInInventory(player, Items.BUCKET) == 1, "the empty bucket did not come back");

        // Random ticks until it curdles; the 1.12 curve takes three, so this has room to spare.
        for (int i = 0; i < 8 && helper.getBlockState(CENTRE).getValue(CheeseMakerBlock.STAGE) != CheeseMakerBlock.DONE; i++) {
            helper.getBlockState(CENTRE).randomTick(helper.getLevel(), pos, helper.getLevel().getRandom());
        }
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(CheeseMakerBlock.STAGE) == CheeseMakerBlock.DONE, "the cheese maker never finished");

        helper.getBlockState(CENTRE).attack(helper.getLevel(), pos, player);
        helper.assertTrue(droppedCount(helper, CuisineBlocks.CHEESE_BLOCK.get().asItem()) == 1, "punching the finished cheese maker did not drop a cheese block");
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(CheeseMakerBlock.STAGE) == 0, "the cheese maker did not reset");
        helper.succeed();
    }

    /** An empty hand must not start the machine, or it would make cheese out of nothing. */
    private static void cheeseMakerIgnoresEmptyHand(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), helper.absolutePos(CENTRE));
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(CheeseMakerBlock.STAGE) == 0, "an empty hand started the cheese maker");
        helper.succeed();
    }

    private static void butterChurnMakesButter(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.BUTTER_CHURN.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));
        BlockPos pos = helper.absolutePos(CENTRE);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(ButterChurnBlock.FULL), "the churn did not fill");
        helper.assertTrue(countInInventory(player, Items.BUCKET) == 1, "the empty bucket did not come back");

        helper.getBlockState(CENTRE).attack(helper.getLevel(), pos, player);
        int butter = droppedCount(helper, CuisineItems.BUTTER.get());
        helper.assertTrue(butter >= 1 && butter <= 3, "a full churn dropped " + butter + " butter, expected 1 to 3");
        helper.assertFalse(helper.getBlockState(CENTRE).getValue(ButterChurnBlock.FULL), "the churn did not empty");
        helper.succeed();
    }

    private static void chocolateMouldMakesBars(GameTestHelper helper) {
        // The mould needs something solid under it or it pops off.
        helper.setBlock(CENTRE.below(), Blocks.STONE);
        helper.setBlock(CENTRE, CuisineBlocks.CHOCOLATE_BAR_MOULD.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(CuisineItems.HOT_CHOCOLATE.get()));
        BlockPos pos = helper.absolutePos(CENTRE);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(ChocolateBarMouldBlock.STAGE) > 0, "the mould did not take the chocolate");
        helper.assertTrue(countInInventory(player, Items.BOWL) == 1, "the empty bowl did not come back");

        for (int i = 0; i < 16 && helper.getBlockState(CENTRE).getValue(ChocolateBarMouldBlock.STAGE) != ChocolateBarMouldBlock.DONE; i++) {
            helper.getBlockState(CENTRE).randomTick(helper.getLevel(), pos, helper.getLevel().getRandom());
        }

        BlockState done = helper.getBlockState(CENTRE);
        helper.assertTrue(done.getValue(ChocolateBarMouldBlock.STAGE) == ChocolateBarMouldBlock.DONE, "the chocolate never set");

        done.attack(helper.getLevel(), pos, player);
        helper.assertTrue(droppedCount(helper, CuisineItems.CHOCOLATE_BAR.get()) == 2, "a finished mould did not drop two bars");
        helper.succeed();
    }
}
