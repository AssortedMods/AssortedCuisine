package com.grim3212.assorted.cuisine.common.item;

import com.grim3212.assorted.lib.core.item.ItemDescription;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;

/**
 * Bandages and health packs: held for a moment to apply, then they heal directly, with no effect on
 * hunger. Healing is in half hearts.
 */
public class HealingItem extends Item {

    /** Long enough to be interrupted by a hit, short enough not to be annoying. */
    public static final Consumable APPLYING = Consumable.builder()
            .consumeSeconds(1.2F)
            .animation(ItemUseAnimation.BRUSH)
            .sound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .hasConsumeParticles(false)
            .build();

    private final float healAmount;

    public HealingItem(float healAmount, Properties props) {
        super(props.component(LibDataComponents.DESCRIPTION.get(), new ItemDescription(SodaItem.healthLine(healAmount))));
        this.healAmount = healAmount;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // Nothing to heal means nothing is spent - the 1.12 version silently ate the item.
        if (player.getHealth() >= player.getMaxHealth()) {
            return InteractionResult.PASS;
        }

        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            entity.heal(this.healAmount);
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
