package com.grim3212.assorted.sodas.gametest;

import com.grim3212.assorted.lib.core.item.LibDataComponents;
import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.sodas.gametest.SodasTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Sodas are drunk: held for a moment rather than swallowed on the click, which is what 1.12 did
 * and what the port kept doing by accident.
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
        out.accept("every_soda_heals_what_the_site_lists", DrinkTests::everySodaHealsWhatTheSiteLists);
    }

    /** Half hearts healed by each soda, as the site's table lists them in hearts. */
    private static final Map<Supplier<Item>, Float> HEALS = Map.of(
            SodasItems.SODA_CARBONATED_WATER::get, 2.0F, SodasItems.SODA_SLURM::get, 3.0F, SodasItems.SODA_ROOT_BEER::get, 5.0F,
            SodasItems.SODA_MUSHROOM::get, 5.0F, SodasItems.SODA_ORANGE::get, 8.0F, SodasItems.SODA_APPLE::get, 10.0F,
            SodasItems.SODA_CREAM_ORANGE::get, 10.0F, SodasItems.SODA_COCOA::get, 14.0F, SodasItems.SODA_GOLDEN_APPLE::get, 20.0F,
            SodasItems.SODA_DIAMOND::get, 20.0F);

    /** From one half heart, so even the largest heal has room to show in full. */
    private static void everySodaHealsWhatTheSiteLists(GameTestHelper helper) {
        List<String> problems = new ArrayList<>();
        for (Map.Entry<Supplier<Item>, Float> soda : HEALS.entrySet()) {
            ServerPlayer player = survivalPlayer(helper, new ItemStack(soda.getKey().get()));
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
            player.setHealth(1.0F);
            player.getMainHandItem().finishUsingItem(helper.getLevel(), player);
            float healed = player.getHealth() - 1.0F;
            if (Math.abs(healed - soda.getValue()) > 0.01F) {
                problems.add(soda.getKey().get() + " healed " + healed + " half hearts, not " + soda.getValue());
            }
        }
        helper.assertTrue(problems.isEmpty(), String.join("\n", problems));
        helper.succeed();
    }

    private static void sodaIsDrunkNotSwallowed(GameTestHelper helper) {
        ServerPlayer player = hurtPlayer(helper, new ItemStack(SodasItems.SODA_APPLE.get(), 2));
        ItemStack stack = player.getMainHandItem();

        helper.assertTrue(stack.getUseAnimation() == UseAnim.DRINK, "the soda is not drunk");

        InteractionResult result = stack.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).getResult();
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
        helper.assertTrue(player.hasEffect(MobEffects.MOVEMENT_SPEED), "cocoa soda did not give speed");
        helper.succeed();
    }

    /**
     * Drunk by a pig rather than by the test player: a player whose connection has not reported a
     * loaded client is invulnerable to everything, so no damage would ever land on one here.
     */
    private static void spikedSodaHurts(GameTestHelper helper) {
        Pig pig = helper.spawn(EntityType.PIG, CENTRE);
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
