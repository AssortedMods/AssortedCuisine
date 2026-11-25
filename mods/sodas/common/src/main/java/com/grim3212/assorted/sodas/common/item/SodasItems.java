package com.grim3212.assorted.sodas.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.sodas.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

import java.util.List;

import java.util.function.Function;
import java.util.function.Supplier;

public class SodasItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    // The bottle and the CO2 canister are ingredients rather than drinks, so they are plain items.
    public static final IRegistryObject<Item> SODA_BOTTLE = register("soda_bottle", props -> new Item(props.stacksTo(16)));
    public static final IRegistryObject<Item> SODA_CO2 = register("soda_co2", props -> new Item(props.stacksTo(16)));
    public static final IRegistryObject<Item> SODA_CARBONATED_WATER = soda("soda_carbonated_water", 2.0F);
    public static final IRegistryObject<Item> SODA_APPLE = soda("soda_apple", 10.0F);
    public static final IRegistryObject<Item> SODA_GOLDEN_APPLE = soda("soda_golden_apple", 20.0F, effect(MobEffects.ABSORPTION, 30, 0));
    public static final IRegistryObject<Item> SODA_DIAMOND = soda("soda_diamond", 20.0F, effect(MobEffects.DAMAGE_RESISTANCE, 20, 0));
    // Caffeine.
    public static final IRegistryObject<Item> SODA_COCOA = soda("soda_cocoa", 14.0F, effect(MobEffects.MOVEMENT_SPEED, 20, 0));
    public static final IRegistryObject<Item> SODA_ORANGE = soda("soda_orange", 8.0F);
    public static final IRegistryObject<Item> SODA_CREAM_ORANGE = soda("soda_cream_orange", 10.0F, effect(MobEffects.SATURATION, 1, 0));
    public static final IRegistryObject<Item> SODA_ROOT_BEER = soda("soda_root_beer", 5.0F, effect(MobEffects.REGENERATION, 5, 0));
    public static final IRegistryObject<Item> SODA_MUSHROOM = soda("soda_mushroom", 5.0F, effect(MobEffects.CONFUSION, 8, 0));
    // Highly addictive, and it takes as much out of you as it puts in.
    public static final IRegistryObject<Item> SODA_SLURM = soda("soda_slurm", 3.0F, effect(MobEffects.MOVEMENT_SPEED, 10, 1), effect(MobEffects.HUNGER, 20, 0));
    // Spiked orange is the one you hand to someone you have a grudge against.
    public static final IRegistryObject<Item> SODA_SPIKED_ORANGE = soda("soda_spiked_orange", -8.0F, effect(MobEffects.POISON, 8, 0));

    /** A certainty of drinking it. A fresh instance each time, as an effect instance counts itself down once applied. */
    private static Supplier<MobEffectInstance> effect(Holder<MobEffect> effect, int seconds, int amplifier) {
        return () -> new MobEffectInstance(effect, seconds * 20, amplifier);
    }

    @SafeVarargs
    private static IRegistryObject<Item> soda(String name, float healAmount, Supplier<MobEffectInstance>... effects) {
        return register(name, props -> new SodaItem(healAmount, List.of(effects), props.stacksTo(16)));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    public static void init() {
    }
}
