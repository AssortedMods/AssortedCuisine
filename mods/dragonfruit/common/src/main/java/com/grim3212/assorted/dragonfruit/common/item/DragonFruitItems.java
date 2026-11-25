package com.grim3212.assorted.dragonfruit.common.item;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class DragonFruitItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<Item> DRAGON_FRUIT = food("dragon_fruit", 4, 0.3F);

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
