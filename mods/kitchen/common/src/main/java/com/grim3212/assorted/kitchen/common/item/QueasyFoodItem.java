package com.grim3212.assorted.kitchen.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Raw food that may leave you hungrier. The chance is rolled here rather than through the food's own effect list,
 * which each loader spells differently.
 */
public class QueasyFoodItem extends Item {

    private final int hungerTicks;
    private final float chance;

    public QueasyFoodItem(int hungerTicks, float chance, Properties props) {
        super(props);
        this.hungerTicks = hungerTicks;
        this.chance = chance;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity.getRandom().nextFloat() < this.chance) {
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, this.hungerTicks, 0));
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
