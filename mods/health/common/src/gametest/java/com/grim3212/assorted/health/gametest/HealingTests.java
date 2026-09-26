package com.grim3212.assorted.health.gametest;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Bandages and health packs are consumables: held for a moment rather than used up on the click, which
 * is what 1.12 did and what the port kept doing by accident.
 */
final class HealingTests {

    private HealingTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("healing_item_takes_time", HealingTests::healingItemTakesTime);
        out.accept("healing_item_spares_a_full_player", HealingTests::healingItemSparesAFullPlayer);
        out.accept("healing_items_describe_their_healing", HealingTests::describeTheirHealing);
    }

    /** A bandage that healed on the click was a better panic button than a golden apple. */
    private static void healingItemTakesTime(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(HealthItems.BANDAGE.get(), 2));
        ItemStack stack = player.getMainHandItem();

        InteractionResult result = stack.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(result.consumesAction(), "the bandage was not used");
        helper.assertTrue(player.isUsingItem(), "the bandage healed without being applied");
        helper.assertTrue(player.getUseItemRemainingTicks() > 0, "the bandage takes no time to apply");
        helper.succeed();
    }

    private static void healingItemSparesAFullPlayer(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, new ItemStack(HealthItems.BANDAGE.get()));
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
        List<String> restores = List.of("tooltip." + Constants.MOD_ID + ".restores");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(HealthItems.BANDAGE.get()), LibDataComponents.DESCRIPTION.get()), restores, "a bandage's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(HealthItems.HEALTHPACK_SUPER.get()), LibDataComponents.DESCRIPTION.get()), restores, "a super health pack's tooltip");
        helper.succeed();
    }
}
