package com.grim3212.assorted.health.common.item;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.lib.core.item.ItemDescription;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * Bandages and health packs: held for a moment to apply, then they heal directly, with no effect on
 * hunger. Healing is in half hearts.
 */
public class HealingItem extends Item {

    /** Long enough to be interrupted by a hit, short enough not to be annoying: 1.2 seconds. */
    private static final int APPLY_TICKS = 24;

    private final float healAmount;

    public HealingItem(float healAmount, Properties props) {
        super(props.component(LibDataComponents.DESCRIPTION.get(), new ItemDescription(healthLine(healAmount))));
        this.healAmount = healAmount;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Nothing to heal means nothing is spent - the 1.12 version silently ate the item.
        if (player.getHealth() >= player.getMaxHealth()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return APPLY_TICKS;
    }

    /** A hit while it is going on stops it, so it cannot be used mid fight; hurtTime counts down from the latest hit. */
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticksRemaining) {
        if (entity.hurtTime > 0 && entity.getTicksUsingItem() >= entity.hurtDuration - entity.hurtTime) {
            entity.stopUsingItem();
            return;
        }

        // Vanilla only sounds the eat and drink animations, so the rustle of the dressing is played here,
        // every fourth tick once the first fifth of the time has passed, as vanilla paces its own.
        int elapsed = APPLY_TICKS - ticksRemaining;
        if (elapsed > (int) (APPLY_TICKS * 0.21875F) && ticksRemaining % 4 == 0) {
            playApplying(entity);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            entity.heal(this.healAmount);
        }

        playApplying(entity);
        if (entity instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        stack.consume(1, entity);
        return stack;
    }

    private static void playApplying(LivingEntity entity) {
        entity.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), entity.getRandom().nextBoolean() ? 0.5F : 1.0F, (float) entity.getRandom().triangle(1.0D, 0.2D));
    }

    /** Half hearts as hearts, trimmed so a whole number reads as "5" rather than "5.0". */
    private static Component healthLine(float halfHearts) {
        float hearts = halfHearts / 2.0F;
        String amount = hearts == Math.floor(hearts) ? String.valueOf((int) hearts) : String.valueOf(hearts);

        return Component.translatable("tooltip." + Constants.MOD_ID + ".restores", amount).withStyle(ChatFormatting.GRAY);
    }
}
