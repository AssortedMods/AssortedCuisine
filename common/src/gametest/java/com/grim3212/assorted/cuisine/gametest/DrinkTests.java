package com.grim3212.assorted.cuisine.gametest;

import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.cuisine.gametest.CuisineTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Sodas, and the healing items that work the same way. All of them are consumables: they are held
 * for a moment rather than swallowed on the click, which is what 1.12 did and what the port kept
 * doing by accident - the items carried a CONSUMABLE component that {@code use} never reached.
 */
final class DrinkTests {

    private DrinkTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("soda_is_drunk_not_swallowed", DrinkTests::sodaIsDrunkNotSwallowed);
        out.accept("soda_heals_when_finished", DrinkTests::sodaHealsWhenFinished);
        out.accept("soda_flavour_applies_its_effect", DrinkTests::sodaFlavourAppliesItsEffect);
        out.accept("spiked_soda_hurts", DrinkTests::spikedSodaHurts);
        out.accept("healing_item_takes_time", DrinkTests::healingItemTakesTime);
        out.accept("healing_item_spares_a_full_player", DrinkTests::healingItemSparesAFullPlayer);
        out.accept("drinks_and_healing_items_describe_their_healing", DrinkTests::describeTheirHealing);
    }

    private static void sodaIsDrunkNotSwallowed(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(CuisineItems.SODA_APPLE.get(), 2));
        ItemStack stack = player.getMainHandItem();

        helper.assertTrue(stack.has(DataComponents.CONSUMABLE), "the soda has no consumable component");

        InteractionResult result = stack.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(result.consumesAction(), "the soda was not used");
        helper.assertTrue(player.isUsingItem(), "drinking the soda did not start the use animation");
        helper.assertTrue(player.getMainHandItem().getCount() == 2, "the soda was swallowed before the animation finished");
        helper.succeed();
    }

    private static void sodaHealsWhenFinished(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(CuisineItems.SODA_APPLE.get()));
        float before = player.getHealth();

        player.getMainHandItem().finishUsingItem(helper.getLevel(), player);
        helper.assertTrue(player.getHealth() > before, "finishing an apple soda did not heal");
        helper.succeed();
    }

    /** Ten flavours that only differed by a number now differ by what they do to you. */
    private static void sodaFlavourAppliesItsEffect(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(CuisineItems.SODA_COCOA.get()));

        player.getMainHandItem().finishUsingItem(helper.getLevel(), player);
        helper.assertTrue(player.hasEffect(MobEffects.SPEED), "cocoa soda did not give speed");
        helper.succeed();
    }

    /**
     * Drunk by a pig rather than by the test player: a player whose connection has not reported a
     * loaded client is invulnerable to everything, so no damage would ever land on one here.
     */
    private static void spikedSodaHurts(GameTestHelper helper) {
        Pig pig = helper.spawn(EntityTypes.PIG, CENTRE);
        float before = pig.getHealth();

        new ItemStack(CuisineItems.SODA_SPIKED_ORANGE.get()).finishUsingItem(helper.getLevel(), pig);
        helper.assertTrue(pig.getHealth() < before, "the spiked orange did not hurt");
        helper.assertTrue(pig.hasEffect(MobEffects.POISON), "the spiked orange did not poison");
        helper.succeed();
    }

    /** A bandage that healed on the click was a better panic button than a golden apple. */
    private static void healingItemTakesTime(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(CuisineItems.BANDAGE.get(), 2));
        ItemStack stack = player.getMainHandItem();

        InteractionResult result = stack.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(result.consumesAction(), "the bandage was not used");
        helper.assertTrue(player.isUsingItem(), "the bandage healed without being applied");
        helper.assertTrue(player.getUseItemRemainingTicks() > 0, "the bandage takes no time to apply");
        helper.succeed();
    }

    private static void healingItemSparesAFullPlayer(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, new ItemStack(CuisineItems.BANDAGE.get()));
        player.setHealth(player.getMaxHealth());

        InteractionResult result = player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertFalse(result.consumesAction(), "a bandage was spent at full health");
        helper.assertTrue(player.getMainHandItem().getCount() == 1, "a bandage was consumed at full health");
        helper.succeed();
    }

    /** Half health, so anything that heals has room to show it. */
    private static ServerPlayer hurtPlayer(GameTestHelper helper, ItemStack held) {
        ServerPlayer player = survivalPlayer(helper, held);
        player.setHealth(player.getMaxHealth() / 2.0F);
        return player;
    }

    /** The heal line comes from a default description component, not an item tooltip override. */
    private static void describeTheirHealing(GameTestHelper helper) {
        List<String> restores = List.of("tooltip.assortedcuisine.restores");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(CuisineItems.SODA_APPLE.get()), LibDataComponents.DESCRIPTION.get()), restores, "an apple soda's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(CuisineItems.BANDAGE.get()), LibDataComponents.DESCRIPTION.get()), restores, "a bandage's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(CuisineItems.SODA_SPIKED_ORANGE.get()), LibDataComponents.DESCRIPTION.get()),
                List.of("tooltip.assortedcuisine.hurts"), "a spiked orange soda's tooltip");
        helper.succeed();
    }
}
