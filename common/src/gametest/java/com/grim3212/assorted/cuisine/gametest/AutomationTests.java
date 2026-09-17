package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineMachineBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.cuisine.common.block.blockentity.CuisineMachineBlockEntity.*;
import static com.grim3212.assorted.cuisine.gametest.CuisineTestSupport.*;

/**
 * Feeding and draining a machine without a player. The machines are {@code WorldlyContainer}s, so a
 * hopper above pushes into the input, a hopper below pulls the output and the empty bucket, and a
 * dropper pointed at one loads it. That is the whole of it: a dropper transfers into any container,
 * so nothing here has to know which items a pack's recipes use.
 */
final class AutomationTests {

    private AutomationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("machine_takes_input_from_above_only", AutomationTests::machineTakesInputFromAboveOnly);
        out.accept("machine_gives_output_downward_only", AutomationTests::machineGivesOutputDownwardOnly);
        out.accept("machine_runs_what_a_hopper_pushed_in", AutomationTests::machineRunsWhatAHopperPushedIn);
        out.accept("machine_refuses_what_it_has_no_recipe_for", AutomationTests::machineRefusesWhatItHasNoRecipeFor);
        out.accept("dropper_loads_a_machine", AutomationTests::dropperLoadsAMachine);
        out.accept("empty_bucket_can_be_hoppered_out", AutomationTests::emptyBucketCanBeHopperedOut);
        out.accept("multi_use_container_is_not_consumed", AutomationTests::multiUseContainerIsNotConsumed);
    }

    /** What a hopper above is allowed to push, which is the input slot and nothing else. */
    private static void machineTakesInputFromAboveOnly(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        CuisineMachineBlockEntity entity = machine(helper, CENTRE);
        ItemStack milk = new ItemStack(Items.MILK_BUCKET);

        helper.assertTrue(Arrays.equals(entity.getSlotsForFace(Direction.UP), new int[]{SLOT_INPUT}), "a hopper above sees the wrong slots");
        helper.assertTrue(entity.canPlaceItemThroughFace(SLOT_INPUT, milk, Direction.UP), "a hopper above cannot fill the input");
        helper.assertFalse(entity.canPlaceItemThroughFace(SLOT_OUTPUT, milk, Direction.UP), "a hopper can push into the output slot");
        helper.assertFalse(entity.canPlaceItemThroughFace(SLOT_CONTAINER, milk, Direction.UP), "a hopper can push into the container slot");
        helper.succeed();
    }

    /** And what a hopper below may take: the results, never the milk waiting to be used. */
    private static void machineGivesOutputDownwardOnly(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        CuisineMachineBlockEntity entity = machine(helper, CENTRE);
        ItemStack any = new ItemStack(Items.BUCKET);

        helper.assertTrue(Arrays.equals(entity.getSlotsForFace(Direction.DOWN), new int[]{SLOT_OUTPUT, SLOT_CONTAINER}), "a hopper below sees the wrong slots");
        helper.assertTrue(entity.canTakeItemThroughFace(SLOT_OUTPUT, any, Direction.DOWN), "a hopper below cannot take the output");
        helper.assertTrue(entity.canTakeItemThroughFace(SLOT_CONTAINER, any, Direction.DOWN), "a hopper below cannot take the empty bucket");
        helper.assertFalse(entity.canTakeItemThroughFace(SLOT_INPUT, any, Direction.DOWN), "a hopper can steal the input before it is used");
        helper.succeed();
    }

    /** The whole loop, driven through the container the way a hopper drives it. */
    private static void machineRunsWhatAHopperPushedIn(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        CuisineMachineBlockEntity entity = machine(helper, CENTRE);

        entity.setItem(SLOT_INPUT, new ItemStack(Items.MILK_BUCKET));
        runMachine(helper, CENTRE, 2000);

        entity = machine(helper, CENTRE);
        helper.assertTrue(entity.getItem(SLOT_OUTPUT).is(CuisineBlocks.CHEESE_BLOCK.get().asItem()), "no cheese reached the output slot");
        helper.assertTrue(entity.getItem(SLOT_CONTAINER).is(Items.BUCKET), "the empty bucket did not reach the container slot");
        helper.assertTrue(entity.getItem(SLOT_INPUT).isEmpty(), "the milk bucket was not used up");

        // A hopper underneath drains both, which is an ordinary extract on those two slots.
        helper.assertTrue(entity.removeItem(SLOT_OUTPUT, 1).is(CuisineBlocks.CHEESE_BLOCK.get().asItem()), "the cheese could not be pulled out");
        helper.assertTrue(entity.removeItem(SLOT_CONTAINER, 1).is(Items.BUCKET), "the bucket could not be pulled out");
        helper.succeed();
    }

    private static void machineRefusesWhatItHasNoRecipeFor(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        CuisineMachineBlockEntity entity = machine(helper, CENTRE);

        helper.assertFalse(entity.canPlaceItem(SLOT_INPUT, new ItemStack(Items.COBBLESTONE)), "the cheese maker accepted cobblestone");
        // Hot chocolate is a machine ingredient, but the mould's, not this one's.
        helper.assertFalse(entity.canPlaceItem(SLOT_INPUT, new ItemStack(com.grim3212.assorted.cuisine.common.item.CuisineItems.HOT_CHOCOLATE.get())),
                "the cheese maker accepted the chocolate mould's ingredient");
        helper.succeed();
    }

    /**
     * A dropper above a machine loads it. Driven through the same two vanilla statics a dropper and
     * a hopper both call, so this is the real transfer path rather than an imitation of it.
     */
    private static void dropperLoadsAMachine(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());

        Container target = HopperBlockEntity.getContainerAt(helper.getLevel(), helper.absolutePos(CENTRE));
        helper.assertTrue(target != null, "a hopper or dropper cannot see the cheese maker as a container");

        // A dropper above pushes down, so the milk enters through the machine's top face.
        ItemStack left = HopperBlockEntity.addItem(null, target, new ItemStack(Items.MILK_BUCKET), Direction.UP);

        helper.assertTrue(left.isEmpty(), "the dropper could not hand the milk over");
        helper.assertTrue(machine(helper, CENTRE).getItem(SLOT_INPUT).is(Items.MILK_BUCKET), "the milk did not reach the input slot");
        helper.assertTrue(droppedCount(helper, Items.MILK_BUCKET) == 0, "the milk ended up on the floor");
        helper.succeed();
    }

    /**
     * The bucket a run hands back has to be reachable from outside, or an automated line silently
     * fills up with buckets nobody can get at.
     */
    private static void emptyBucketCanBeHopperedOut(GameTestHelper helper) {
        helper.setBlock(CENTRE, CuisineBlocks.CHEESE_MAKER.get());
        CuisineMachineBlockEntity entity = machine(helper, CENTRE);

        entity.setItem(SLOT_INPUT, new ItemStack(Items.MILK_BUCKET));
        runMachine(helper, CENTRE, 2000);

        entity = machine(helper, CENTRE);
        helper.assertTrue(entity.getItem(SLOT_CONTAINER).is(Items.BUCKET), "the empty bucket was not handed back");

        // Exactly what a hopper underneath asks: the down face, then the slot it was offered.
        helper.assertTrue(Arrays.stream(entity.getSlotsForFace(Direction.DOWN)).anyMatch(slot -> slot == SLOT_CONTAINER),
                "a hopper below is never offered the bucket slot");
        helper.assertTrue(entity.canTakeItemThroughFace(SLOT_CONTAINER, entity.getItem(SLOT_CONTAINER), Direction.DOWN),
                "a hopper below may not take the bucket");
        helper.assertTrue(entity.removeItem(SLOT_CONTAINER, 1).is(Items.BUCKET), "the bucket could not be pulled out");
        helper.succeed();
    }

    /**
     * A container whose remainder depends on what is left in it - Assorted Tools' multi-use milk
     * buckets - must come back rather than be swallowed. The knife stands in for one here: it has
     * the same per-stack remainder shape and this mod ships it, so the test needs no other mod.
     */
    private static void multiUseContainerIsNotConsumed(GameTestHelper helper) {
        ItemStack knife = new ItemStack(CuisineItems.KNIFE.get());
        knife.setDamageValue(5);

        ItemStack remainder = CuisineMachineBlockEntity.remainderFor(knife);

        helper.assertFalse(remainder.isEmpty(), "a part-used container was swallowed instead of handed back");
        helper.assertTrue(remainder.is(CuisineItems.KNIFE.get()), "the remainder was not the same container");
        helper.assertTrue(remainder.getDamageValue() == 6,
                "the remainder ignored what the stack had left: expected one more use spent, got damage " + remainder.getDamageValue());

        // A plain container still answers the ordinary way.
        helper.assertTrue(CuisineMachineBlockEntity.remainderFor(new ItemStack(Items.MILK_BUCKET)).is(Items.BUCKET),
                "a milk bucket did not leave an empty bucket");
        helper.succeed();
    }
}
