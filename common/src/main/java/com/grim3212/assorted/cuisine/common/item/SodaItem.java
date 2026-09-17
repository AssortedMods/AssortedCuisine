package com.grim3212.assorted.cuisine.common.item;

import com.grim3212.assorted.cuisine.api.CuisineDamageTypes;
import com.grim3212.assorted.lib.core.item.ItemDescription;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A bottle of soda. Each flavour is its own item now - 1.12 packed all thirteen into one item's
 * damage value, which modern Minecraft has no equivalent for.
 */
public class SodaItem extends Item {

    private final float healAmount;

    public SodaItem(float healAmount, Properties props) {
        // Thirteen near-identical bottles need a line saying which one this is.
        super(props.component(LibDataComponents.DESCRIPTION.get(), new ItemDescription(healthLine(healAmount))));
        this.healAmount = healAmount;
    }

    /**
     * Hands off to the consumable so the drinking animation, sound and particles all happen. 1.12
     * drank it instantly, and so did the port until now: overriding {@code use} outright skipped
     * the CONSUMABLE component the item was already carrying.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // A healing soda on full health is not worth the bottle; a harmful one always is.
        if (this.healAmount > 0.0F && player.getHealth() >= player.getMaxHealth()) {
            return InteractionResult.PASS;
        }

        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (this.healAmount < 0.0F) {
                entity.hurtServer(serverLevel, CuisineDamageTypes.source(serverLevel, CuisineDamageTypes.SPIKED_SODA), -this.healAmount);
            } else {
                entity.heal(this.healAmount);
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }

    /**
     * Half hearts as hearts, trimmed so a whole number reads as "5" rather than "5.0".
     */
    static Component healthLine(float halfHearts) {
        float hearts = Math.abs(halfHearts) / 2.0F;
        String amount = hearts == Math.floor(hearts) ? String.valueOf((int) hearts) : String.valueOf(hearts);

        return halfHearts < 0.0F
                ? Component.translatable("tooltip.assortedcuisine.hurts", amount).withStyle(ChatFormatting.RED)
                : Component.translatable("tooltip.assortedcuisine.restores", amount).withStyle(ChatFormatting.GRAY);
    }
}
