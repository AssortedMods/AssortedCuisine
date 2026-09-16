package com.grim3212.assorted.cuisine.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A bottle of soda. Each flavour is its own item now - 1.12 packed all thirteen into one item's
 * damage value, which modern Minecraft has no equivalent for.
 *
 * <p>A flavour either heals or, in spiked orange's case, hurts; the amount is in half hearts.
 */
public class SodaItem extends Item {

    private final float healAmount;

    public SodaItem(float healAmount, Properties props) {
        super(props);
        this.healAmount = healAmount;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.healAmount > 0.0F && player.getHealth() >= player.getMaxHealth()) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            if (this.healAmount < 0.0F) {
                player.hurtServer(serverLevel, player.damageSources().generic(), -this.healAmount);
            } else {
                player.heal(this.healAmount);
            }
        }

        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
