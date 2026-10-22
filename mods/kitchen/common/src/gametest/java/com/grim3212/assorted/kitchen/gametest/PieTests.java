package com.grim3212.assorted.kitchen.gametest;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.lib.test.TestSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CakeBlock;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** The baked pies as food you place, and the cheese sandwich being worth more than its cheese. */
final class PieTests {

    private static final BlockPos PIE = new BlockPos(2, 1, 2);

    private PieTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("pies_are_eaten_a_slice_at_a_time", PieTests::piesAreEatenASliceAtATime);
        out.accept("cheese_sandwich_is_better_food_than_cheese", PieTests::cheeseSandwichIsBetterFoodThanCheese);
    }

    private static void piesAreEatenASliceAtATime(GameTestHelper helper) {
        helper.setBlock(PIE, KitchenBlocks.APPLE_PIE.get());
        ServerPlayer player = TestSupport.survivalPlayer(helper, ItemStack.EMPTY);
        player.getFoodData().setFoodLevel(10);

        TestSupport.rightClick(player, helper.getLevel(), ItemStack.EMPTY, helper.absolutePos(PIE));
        helper.assertTrue(player.getFoodData().getFoodLevel() > 10, "eating a slice of apple pie did not feed a hungry player");
        helper.assertValueEqual(helper.getBlockState(PIE).getValue(CakeBlock.BITES), 1, "slices eaten from the apple pie");
        helper.succeed();
    }

    private static void cheeseSandwichIsBetterFoodThanCheese(GameTestHelper helper) {
        FoodProperties cheese = new ItemStack(KitchenItems.CHEESE.get()).get(DataComponents.FOOD);
        FoodProperties sandwich = new ItemStack(KitchenItems.HOT_CHEESE.get()).get(DataComponents.FOOD);
        helper.assertTrue(cheese != null && sandwich != null, "cheese or the cheese sandwich is not food");
        helper.assertTrue(sandwich.nutrition() > cheese.nutrition(), "the cheese sandwich fills " + sandwich.nutrition() + ", no more than cheese's " + cheese.nutrition());
        helper.succeed();
    }
}
