package com.grim3212.assorted.cuisine.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A bowl of chocolate, hot or cold. Drinking it heals on top of feeding you, which is why it needs
 * a class at all - the rest (the drinking animation, the bowl left behind) is components.
 */
public class ChocolateBowlItem extends Item {

    private static final float HEAL_AMOUNT = 4.0F;

    public ChocolateBowlItem(Properties props) {
        super(props);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            entity.heal(HEAL_AMOUNT);
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
