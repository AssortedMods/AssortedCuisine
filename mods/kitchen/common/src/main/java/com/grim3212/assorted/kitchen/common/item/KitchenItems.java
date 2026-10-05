package com.grim3212.assorted.kitchen.common.item;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.function.Function;

public class KitchenItems {

    // Raw eggs and raw pastry make you hungrier; cooking them is most of what this mod is for.
    private static final Consumable RAW_EGG = queasy(600, 0.3F);
    private static final Consumable RAW_PASTRY = queasy(300, 0.1F);

    // --- Dairy ---
    public static final IRegistryObject<Item> BUTTER = food("butter", 2, 0.4F);
    public static final IRegistryObject<Item> CHEESE = food("cheese", 3, 0.6F);
    public static final IRegistryObject<Item> BREAD_SLICE = food("bread_slice", 2, 0.4F);
    public static final IRegistryObject<Item> CHEESE_BURGER = food("cheese_burger", 12, 0.95F);
    public static final IRegistryObject<Item> HOT_CHEESE = food("hot_cheese", 8, 0.75F);
    public static final IRegistryObject<Item> EGGS_UNMIXED = food("eggs_unmixed", 2, 0.1F, RAW_EGG);
    public static final IRegistryObject<Item> EGGS_MIXED = food("eggs_mixed", 4, 0.4F, RAW_EGG);
    public static final IRegistryObject<Item> EGGS_COOKED = food("eggs_cooked", 10, 0.8F);

    // --- Kitchen tools ---
    public static final IRegistryObject<Item> KNIFE = register("knife", props -> new KitchenToolItem(props.stacksTo(1).durability(64)));
    public static final IRegistryObject<Item> WHISK = register("whisk", props -> new KitchenToolItem(props.stacksTo(1).durability(64)));
    public static final IRegistryObject<Item> PAN = register("pan", props -> new Item(props.stacksTo(16)));

    // --- Chocolate ---
    public static final IRegistryObject<Item> MORTAR_AND_PESTLE = register("mortar_and_pestle", props -> new KitchenToolItem(props.stacksTo(1).durability(64)));
    public static final IRegistryObject<Item> COCOA_DUST = register("cocoa_dust", props -> new Item(props));
    public static final IRegistryObject<Item> CHOCOLATE_BOWL = register("chocolate_bowl", props -> new Item(drink(props.stacksTo(16), 4, 0.3F)));
    public static final IRegistryObject<Item> HOT_CHOCOLATE = register("hot_chocolate", props -> new Item(drink(props.stacksTo(1), 6, 0.6F).craftRemainder(Items.BOWL)));
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
    private static Item.Properties drink(Item.Properties props, int nutrition, float saturation) {
        return props.food(new FoodProperties(nutrition, saturation, false), Consumables.defaultDrink().build())
                .usingConvertsTo(Items.BOWL);
    }

    private static Consumable queasy(int durationTicks, float probability) {
        return Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, durationTicks, 0), probability)).build();
    }

    private static IRegistryObject<Item> rawPie(String name) {
        return food(name, 2, 0.3F, RAW_PASTRY);
    }

    private static IRegistryObject<Item> food(String name, int nutrition, float saturation) {
        return register(name, props -> new Item(props.food(new FoodProperties(nutrition, saturation, false))));
    }

    private static IRegistryObject<Item> food(String name, int nutrition, float saturation, Consumable consumable) {
        return register(name, props -> new Item(props.food(new FoodProperties(nutrition, saturation, false), consumable)));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        // Since 1.21.2 every item has to know its own id before it is constructed, so the
        // properties are built here where the registration name is known.
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return KitchenBlocks.ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
