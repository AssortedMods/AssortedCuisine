package com.grim3212.assorted.sodas.common.item;

import com.grim3212.assorted.lib.core.item.ItemDescription;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.api.SodasDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

/**
 * A bottle of soda. Each flavor is its own item now - 1.12 packed all thirteen into one item's
 * damage value, which modern Minecraft has no equivalent for.
 */
public class SodaItem extends Item {

    /** As long as a potion takes to drink. */
    private static final int DRINK_TICKS = 32;

    private final float healAmount;
    private final List<Supplier<MobEffectInstance>> effects;

    public SodaItem(float healAmount, List<Supplier<MobEffectInstance>> effects, Properties props) {
        // Thirteen near-identical bottles need a line saying which one this is.
        super(props.component(LibDataComponents.DESCRIPTION.get(), new ItemDescription(healthLine(healAmount))));
        this.healAmount = healAmount;
        this.effects = List.copyOf(effects);
    }

    /**
     * Drunk over a moment, with the drinking animation and sound, rather than swallowed on the click
     * the way 1.12 did it.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // A healing soda on full health is not worth the bottle; a harmful one always is.
        if (this.healAmount > 0.0F && player.getHealth() >= player.getMaxHealth()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_TICKS;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (this.healAmount < 0.0F) {
                entity.hurt(SodasDamageTypes.source(serverLevel, SodasDamageTypes.SPIKED_SODA), -this.healAmount);
            } else {
                entity.heal(this.healAmount);
            }

            for (Supplier<MobEffectInstance> effect : this.effects) {
                entity.addEffect(effect.get());
            }
        }

        if (entity instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        stack.consume(1, entity);
        return stack;
    }

    /**
     * Half hearts as hearts, trimmed so a whole number reads as "5" rather than "5.0".
     */
    private static Component healthLine(float halfHearts) {
        float hearts = Math.abs(halfHearts) / 2.0F;
        String amount = hearts == Math.floor(hearts) ? String.valueOf((int) hearts) : String.valueOf(hearts);

        return halfHearts < 0.0F
                ? Component.translatable("tooltip." + Constants.MOD_ID + ".hurts", amount).withStyle(ChatFormatting.RED)
                : Component.translatable("tooltip." + Constants.MOD_ID + ".restores", amount).withStyle(ChatFormatting.GRAY);
    }
}
