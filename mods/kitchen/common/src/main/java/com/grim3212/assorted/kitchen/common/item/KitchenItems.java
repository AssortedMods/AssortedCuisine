package com.grim3212.assorted.kitchen.common.item;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class KitchenItems {

    // --- Dairy ---
    public static final IRegistryObject<Item> BUTTER = food("butter", 2, 0.4F);
    public static final IRegistryObject<Item> CHEESE = food("cheese", 3, 0.6F);
    public static final IRegistryObject<Item> BREAD_SLICE = food("bread_slice", 2, 0.4F);
    public static final IRegistryObject<Item> CHEESE_BURGER = food("cheese_burger", 12, 0.95F);
    public static final IRegistryObject<Item> HOT_CHEESE = food("hot_cheese", 8, 0.75F);
    // Raw eggs and raw pastry make you hungrier; cooking them is most of what this mod is for.
    public static final IRegistryObject<Item> EGGS_UNMIXED = rawEgg("eggs_unmixed", 2, 0.1F);
    public static final IRegistryObject<Item> EGGS_MIXED = rawEgg("eggs_mixed", 4, 0.4F);
    public static final IRegistryObject<Item> EGGS_COOKED = food("eggs_cooked", 10, 0.8F);

    // --- Kitchen tools ---
    public static final IRegistryObject<Item> KNIFE = register("knife", props -> new KitchenToolItem(props.stacksTo(1).durability(64)));
    public static final IRegistryObject<Item> WHISK = register("whisk", props -> new KitchenToolItem(props.stacksTo(1).durability(64)));
    public static final IRegistryObject<Item> PAN = register("pan", props -> new Item(props.stacksTo(16)));

    // --- Chocolate ---
    public static final IRegistryObject<Item> MORTAR_AND_PESTLE = register("mortar_and_pestle", props -> new KitchenToolItem(props.stacksTo(1).durability(64)));
    public static final IRegistryObject<Item> COCOA_DUST = register("cocoa_dust", props -> new Item(props));
    public static final IRegistryObject<Item> CHOCOLATE_BOWL = register("chocolate_bowl", props -> new DrinkItem(drink(props.stacksTo(16), 4, 0.3F)));
    public static final IRegistryObject<Item> HOT_CHOCOLATE = register("hot_chocolate", props -> new DrinkItem(drink(props.stacksTo(1), 6, 0.6F).craftRemainder(Items.BOWL)));
    public static final IRegistryObject<Item> CHOCOLATE_BALL = food("chocolate_ball", 2, 0.2F);
    public static final IRegistryObject<Item> CHOCOLATE_BAR = food("chocolate_bar", 3, 0.8F);
    public static final IRegistryObject<Item> CHOCOLATE_BAR_WRAPPED = food("chocolate_bar_wrapped", 5, 0.8F);
    public static final IRegistryObject<Item> WRAPPER = register("wrapper", props -> new Item(props));

    // --- Pies ---
    public static final IRegistryObject<Item> DOUGH = food("dough", 1, 0.2F);
    public static final IRegistryObject<Item> PUMPKIN_SLICE = food("pumpkin_slice", 1, 0.2F);
    public static final IRegistryObject<Item> RAW_EMPTY_PIE = rawPie("raw_empty_pie");
    public static final IRegistryObject<Item> RAW_APPLE_PIE = rawPie("raw_apple_pie");
    public static final IRegistryObject<Item> RAW_MELON_PIE = rawPie("raw_melon_pie");
    public static final IRegistryObject<Item> RAW_PUMPKIN_PIE = rawPie("raw_pumpkin_pie");
    public static final IRegistryObject<Item> RAW_CHOCOLATE_PIE = rawPie("raw_chocolate_pie");
    public static final IRegistryObject<Item> RAW_PORK_PIE = rawPie("raw_pork_pie");

    /**
     * A bowl you drink: it feeds you and leaves the bowl behind. 1.12 had these heal outright, which
     * is how food worked before hunger existed; a modern bowl of something is worth eating instead.
     *
     * <p>Hot chocolate also sets a crafting remainder on top of this: usingConvertsTo only covers
     * being drunk, and the chocolate mold takes the bowl from a recipe-shaped path rather than from
     * an eating animation.
     */
    private static Item.Properties drink(Item.Properties props, int nutrition, float saturationModifier) {
        return props.food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).usingConvertsTo(Items.BOWL).build());
    }

    /** One time in three, a raw egg gives you half a minute of hunger. */
    private static IRegistryObject<Item> rawEgg(String name, int nutrition, float saturationModifier) {
        return register(name, props -> new QueasyFoodItem(600, 0.3F, foodProperties(props, nutrition, saturationModifier)));
    }

    /** One time in ten, raw pastry gives you a quarter of a minute of hunger. */
    private static IRegistryObject<Item> rawPie(String name) {
        return register(name, props -> new QueasyFoodItem(300, 0.1F, foodProperties(props, 2, 0.3F)));
    }

    private static IRegistryObject<Item> food(String name, int nutrition, float saturationModifier) {
        return register(name, props -> new Item(foodProperties(props, nutrition, saturationModifier)));
    }

    /** Saturation is written as vanilla writes it, as a modifier: the food restores nutrition x modifier x 2. */
    private static Item.Properties foodProperties(Item.Properties props, int nutrition, float saturationModifier) {
        return props.food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build());
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        return KitchenBlocks.ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    public static void init() {
    }
}
