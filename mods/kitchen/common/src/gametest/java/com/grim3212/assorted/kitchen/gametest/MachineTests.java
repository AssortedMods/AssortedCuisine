package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.block.KitchenMachineBlock;
import com.grim3212.assorted.kitchen.common.block.blockentity.KitchenMachineBlockEntity;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.kitchen.common.block.blockentity.KitchenMachineBlockEntity.*;
import static com.grim3212.assorted.kitchen.gametest.KitchenTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * The three blocks that turn one thing into another over time: milk into cheese, milk into butter,
 * hot chocolate into bars. Each holds an input, an output and the bucket the recipe left behind, is
 * loaded and emptied by right clicking, and can be fed and drained by a hopper.
 */
final class MachineTests {

    private MachineTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("cheese_maker_makes_cheese", MachineTests::cheeseMakerMakesCheese);
        out.accept("cheese_maker_ignores_empty_hand", MachineTests::cheeseMakerIgnoresEmptyHand);
        out.accept("machine_drives_a_comparator", MachineTests::machineDrivesAComparator);
        out.accept("machine_survives_a_reload", MachineTests::machineSurvivesAReload);
        out.accept("butter_churn_makes_butter", MachineTests::butterChurnMakesButter);
        out.accept("butter_churn_hand_turn_helps", MachineTests::butterChurnHandTurnHelps);
        out.accept("chocolate_mold_makes_bars", MachineTests::chocolateMoldMakesBars);
        out.accept("chocolate_mold_is_faster_on_ice", MachineTests::chocolateMoldIsFasterOnIce);
    }

    private static void cheeseMakerMakesCheese(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.CHEESE_MAKER.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));
        BlockPos pos = helper.absolutePos(CENTRE);

        InteractionResult result = rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        helper.assertTrue(result.consumesAction(), "the milk bucket was not accepted");
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(KitchenMachineBlock.STAGE) == 0, "an unstarted cheese maker already showed progress");

        runMachine(helper, CENTRE, 2000);
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(KitchenMachineBlock.STAGE) == KitchenMachineBlock.DONE, "the cheese maker never finished");

        // Collecting is a right click, and what comes out goes to the player rather than the floor.
        rightClick(player, helper.getLevel(), ItemStack.EMPTY, pos);
        helper.assertTrue(countInInventory(player, KitchenBlocks.CHEESE_BLOCK.get().asItem()) == 1, "the cheese did not reach the player");
        helper.assertTrue(countInInventory(player, Items.BUCKET) == 1, "the empty bucket did not come back");
        helper.assertTrue(droppedCount(helper, KitchenBlocks.CHEESE_BLOCK.get().asItem()) == 0, "the cheese was thrown on the floor");
        helper.assertTrue(helper.getBlockState(CENTRE).getValue(KitchenMachineBlock.STAGE) == 0, "the cheese maker did not reset");
        helper.succeed();
    }

    /** An empty hand must not start the machine, or it would make cheese out of nothing. */
    private static void cheeseMakerIgnoresEmptyHand(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.CHEESE_MAKER.get());
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), helper.absolutePos(CENTRE));
        helper.assertTrue(machine(helper, CENTRE).isEmpty(), "an empty hand put something in the cheese maker");
        helper.succeed();
    }

    /** The whole point of the comparator output: a hopper line can wait for the cheese. */
    private static void machineDrivesAComparator(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.CHEESE_MAKER.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));
        BlockPos pos = helper.absolutePos(CENTRE);

        helper.assertTrue(signal(helper) == 0, "an empty cheese maker gave a signal");

        rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        runMachine(helper, CENTRE, 100);
        int working = signal(helper);
        helper.assertTrue(working > 0 && working < KitchenMachineBlock.DONE, "a working cheese maker read " + working + ", expected between 1 and 14");

        runMachine(helper, CENTRE, 2000);
        helper.assertTrue(signal(helper) == KitchenMachineBlock.DONE, "a cheese maker with cheese waiting did not read full");
        helper.succeed();
    }

    /** Half-made cheese has to survive the chunk being written out and read back. */
    private static void machineSurvivesAReload(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.CHEESE_MAKER.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));

        rightClick(player, helper.getLevel(), player.getMainHandItem(), helper.absolutePos(CENTRE));
        runMachine(helper, CENTRE, 100);

        KitchenMachineBlockEntity reloaded = afterReload(helper, CENTRE, KitchenMachineBlockEntity.class);
        helper.assertTrue(reloaded.isWorking(), "the cheese maker forgot what it was making");
        helper.assertTrue(reloaded.stage() > 0, "the reloaded cheese maker lost its progress");
        helper.succeed();
    }

    private static void butterChurnMakesButter(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.BUTTER_CHURN.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));
        BlockPos pos = helper.absolutePos(CENTRE);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        runMachine(helper, CENTRE, 2000);
        rightClick(player, helper.getLevel(), ItemStack.EMPTY, pos);

        helper.assertTrue(countInInventory(player, KitchenItems.BUTTER.get()) == 2, "a finished churn did not give two butter");
        helper.assertTrue(countInInventory(player, Items.BUCKET) == 1, "the empty bucket did not come back");
        helper.succeed();
    }

    /** Right clicking a working churn turns the handle, which is the one machine you can hurry. */
    private static void butterChurnHandTurnHelps(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.BUTTER_CHURN.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(Items.MILK_BUCKET));
        BlockPos pos = helper.absolutePos(CENTRE);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        runMachine(helper, CENTRE, 40);
        int before = machine(helper, CENTRE).stage();

        rightClick(player, helper.getLevel(), ItemStack.EMPTY, pos);
        helper.assertTrue(machine(helper, CENTRE).stage() > before, "turning the handle did not advance the churn");
        helper.succeed();
    }

    private static void chocolateMoldMakesBars(GameTestHelper helper) {
        helper.setBlock(CENTRE, KitchenBlocks.CHOCOLATE_BAR_MOLD.get());
        ServerPlayer player = survivalPlayer(helper, new ItemStack(KitchenItems.HOT_CHOCOLATE.get()));
        BlockPos pos = helper.absolutePos(CENTRE);

        rightClick(player, helper.getLevel(), player.getMainHandItem(), pos);
        runMachine(helper, CENTRE, 2000);
        rightClick(player, helper.getLevel(), ItemStack.EMPTY, pos);

        helper.assertTrue(countInInventory(player, KitchenItems.CHOCOLATE_BAR.get()) == 2, "a finished mold did not give two bars");
        helper.assertTrue(countInInventory(player, Items.BOWL) == 1, "the empty bowl did not come back");
        helper.succeed();
    }

    /**
     * Cold blocks against the mold's sides each speed it up, so a ringed mold beats a bare one and
     * a fully ringed one beats a half ringed one. Ice underneath is not counted any more - that is
     * where the hopper goes.
     */
    private static void chocolateMoldIsFasterOnIce(GameTestHelper helper) {
        BlockPos bare = CENTRE;
        BlockPos some = CENTRE.east(3);
        BlockPos ringed = CENTRE.east(6);

        for (BlockPos pos : new BlockPos[]{bare, some, ringed}) {
            helper.setBlock(pos, KitchenBlocks.CHOCOLATE_BAR_MOLD.get());
        }

        // Ice below must not count, or the old behaviour would still be passing this.
        helper.setBlock(bare.below(), Blocks.ICE);

        helper.setBlock(some.north(), Blocks.ICE);
        for (Direction side : Direction.Plane.HORIZONTAL) {
            helper.setBlock(ringed.relative(side), Blocks.ICE);
        }

        ServerPlayer player = survivalPlayer(helper, new ItemStack(KitchenItems.HOT_CHOCOLATE.get(), 3));
        for (BlockPos pos : new BlockPos[]{bare, some, ringed}) {
            rightClick(player, helper.getLevel(), player.getMainHandItem(), helper.absolutePos(pos));
            runMachine(helper, pos, 30);
        }

        int bareStage = machine(helper, bare).stage();
        int someStage = machine(helper, some).stage();
        int ringedStage = machine(helper, ringed).stage();

        helper.assertTrue(someStage > bareStage,
                "one ice block beside the mold did not help: bare " + bareStage + ", one " + someStage);
        helper.assertTrue(ringedStage > someStage,
                "a full ring did not beat a single block: one " + someStage + ", ringed " + ringedStage);
        helper.succeed();
    }

    private static int signal(GameTestHelper helper) {
        BlockPos pos = helper.absolutePos(CENTRE);
        return helper.getBlockState(CENTRE).getAnalogOutputSignal(helper.getLevel(), pos, Direction.NORTH);
    }
}
