package com.grim3212.assorted.cuisine.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Bandages and health packs: consumed on use to heal directly, with no eating animation and no
 * effect on hunger. Healing is in half hearts.
 */
public class HealingItem extends Item {

    private final float healAmount;

    public HealingItem(float healAmount, Properties props) {
        super(props);
        this.healAmount = healAmount;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Nothing to heal means nothing is spent - the 1.12 version silently ate the item.
        if (player.getHealth() >= player.getMaxHealth()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            player.heal(this.healAmount);
        }

        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
