package com.grim3212.assorted.sodas.gametest;

import com.grim3212.assorted.lib.core.item.LibDataComponents;
import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
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

import static com.grim3212.assorted.sodas.gametest.SodasTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Sodas are consumables: held for a moment rather than swallowed on the click, which is what 1.12 did
 * and what the port kept doing by accident. The items carried a CONSUMABLE component {@code use} never reached.
 */
final class DrinkTests {

    private DrinkTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("soda_is_drunk_not_swallowed", DrinkTests::sodaIsDrunkNotSwallowed);
        out.accept("soda_heals_when_finished", DrinkTests::sodaHealsWhenFinished);
        out.accept("soda_flavor_applies_its_effect", DrinkTests::sodaFlavorAppliesItsEffect);
        out.accept("spiked_soda_hurts", DrinkTests::spikedSodaHurts);
        out.accept("sodas_describe_their_healing", DrinkTests::describeTheirHealing);
    }

    private static void sodaIsDrunkNotSwallowed(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(SodasItems.SODA_APPLE.get(), 2));
        ItemStack stack = player.getMainHandItem();

        helper.assertTrue(stack.has(DataComponents.CONSUMABLE), "the soda has no consumable component");

        InteractionResult result = stack.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(result.consumesAction(), "the soda was not used");
        helper.assertTrue(player.isUsingItem(), "drinking the soda did not start the use animation");
        helper.assertTrue(player.getMainHandItem().getCount() == 2, "the soda was swallowed before the animation finished");
        helper.succeed();
    }

    private static void sodaHealsWhenFinished(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(SodasItems.SODA_APPLE.get()));
        float before = player.getHealth();

        player.getMainHandItem().finishUsingItem(helper.getLevel(), player);
        helper.assertTrue(player.getHealth() > before, "finishing an apple soda did not heal");
        helper.succeed();
    }

    /** Ten flavors that only differed by a number now differ by what they do to you. */
    private static void sodaFlavorAppliesItsEffect(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(SodasItems.SODA_COCOA.get()));

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

        new ItemStack(SodasItems.SODA_SPIKED_ORANGE.get()).finishUsingItem(helper.getLevel(), pig);
        helper.assertTrue(pig.getHealth() < before, "the spiked orange did not hurt");
        helper.assertTrue(pig.hasEffect(MobEffects.POISON), "the spiked orange did not poison");
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
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(SodasItems.SODA_APPLE.get()), LibDataComponents.DESCRIPTION.get()),
                List.of("tooltip." + Constants.MOD_ID + ".restores"), "an apple soda's tooltip");
        helper.assertValueEqual(tooltipKeys(helper, new ItemStack(SodasItems.SODA_SPIKED_ORANGE.get()), LibDataComponents.DESCRIPTION.get()),
                List.of("tooltip." + Constants.MOD_ID + ".hurts"), "a spiked orange soda's tooltip");
        helper.succeed();
    }
}
