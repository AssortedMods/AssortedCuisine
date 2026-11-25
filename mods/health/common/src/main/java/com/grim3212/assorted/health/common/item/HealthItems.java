package com.grim3212.assorted.health.common.item;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class HealthItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<Item> SWEETS = food("sweets", 2, 0.1F);
    public static final IRegistryObject<Item> POWERED_SUGAR = register("powered_sugar", props -> new Item(props));
    public static final IRegistryObject<Item> POWERED_SWEETS = food("powered_sweets", 6, 0.3F);
    public static final IRegistryObject<Item> BANDAGE = healing("bandage", 3.0F, 16);
    public static final IRegistryObject<Item> HEALTHPACK = healing("healthpack", 5.0F, 4);
    public static final IRegistryObject<Item> HEALTHPACK_SUPER = healing("healthpack_super", 12.0F, 4);

    /** Applied rather than eaten, so it takes a moment and can be interrupted. */
    private static IRegistryObject<Item> healing(String name, float healAmount, int stackSize) {
        return register(name, props -> new HealingItem(healAmount, props.stacksTo(stackSize)));
    }

    /** Saturation is written as vanilla writes it, as a modifier: the food restores nutrition x modifier x 2. */
    private static IRegistryObject<Item> food(String name, int nutrition, float saturationModifier) {
        return register(name, props -> new Item(props.food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build())));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    public static void init() {
    }
}
