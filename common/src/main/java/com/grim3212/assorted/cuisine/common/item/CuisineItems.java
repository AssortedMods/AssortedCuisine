package com.grim3212.assorted.cuisine.common.item;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.function.Function;

public class CuisineItems {

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
    public static final IRegistryObject<Item> KNIFE = register("knife", props -> new KitchenToolItem(props.stacksTo(1).durability(63)));
    public static final IRegistryObject<Item> WHISK = register("whisk", props -> new KitchenToolItem(props.stacksTo(1).durability(63)));
    public static final IRegistryObject<Item> PAN = register("pan", props -> new Item(props.stacksTo(16)));

    // --- Chocolate ---
    public static final IRegistryObject<Item> COCOA_DUST = register("cocoa_dust", props -> new Item(props));
    public static final IRegistryObject<Item> CHOCOLATE_BOWL = register("chocolate_bowl", props -> new ChocolateBowlItem(drinkable(props.stacksTo(16))));
    public static final IRegistryObject<Item> HOT_CHOCOLATE = register("hot_chocolate", props -> new ChocolateBowlItem(drinkable(props.stacksTo(1)).craftRemainder(Items.BOWL)));
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

    // --- Health ---
    public static final IRegistryObject<Item> SWEETS = food("sweets", 2, 0.1F);
    public static final IRegistryObject<Item> POWERED_SUGAR = register("powered_sugar", props -> new Item(props));
    public static final IRegistryObject<Item> POWERED_SWEETS = food("powered_sweets", 6, 0.3F);
    public static final IRegistryObject<Item> BANDAGE = healing("bandage", 3.0F, 16);
    public static final IRegistryObject<Item> HEALTHPACK = healing("healthpack", 5.0F, 4);
    public static final IRegistryObject<Item> HEALTHPACK_SUPER = healing("healthpack_super", 12.0F, 4);

    // --- Dragon fruit ---
    public static final IRegistryObject<Item> DRAGON_FRUIT = food("dragon_fruit", 4, 0.3F);

    // --- Soda ---
    // The bottle and the CO2 canister are ingredients rather than drinks, so they are plain items.
    public static final IRegistryObject<Item> SODA_BOTTLE = register("soda_bottle", props -> new Item(props.stacksTo(16)));
    public static final IRegistryObject<Item> SODA_CO2 = register("soda_co2", props -> new Item(props.stacksTo(16)));
    public static final IRegistryObject<Item> SODA_CARBONATED_WATER = soda("soda_carbonated_water", 2.0F);
    public static final IRegistryObject<Item> SODA_APPLE = soda("soda_apple", 10.0F);
    public static final IRegistryObject<Item> SODA_GOLDEN_APPLE = soda("soda_golden_apple", 20.0F, effect(MobEffects.ABSORPTION, 30, 0));
    public static final IRegistryObject<Item> SODA_DIAMOND = soda("soda_diamond", 20.0F, effect(MobEffects.RESISTANCE, 20, 0));
    // Caffeine.
    public static final IRegistryObject<Item> SODA_COCOA = soda("soda_cocoa", 14.0F, effect(MobEffects.SPEED, 20, 0));
    public static final IRegistryObject<Item> SODA_ORANGE = soda("soda_orange", 8.0F);
    public static final IRegistryObject<Item> SODA_CREAM_ORANGE = soda("soda_cream_orange", 10.0F, effect(MobEffects.SATURATION, 1, 0));
    public static final IRegistryObject<Item> SODA_ROOT_BEER = soda("soda_root_beer", 5.0F, effect(MobEffects.REGENERATION, 5, 0));
    public static final IRegistryObject<Item> SODA_MUSHROOM = soda("soda_mushroom", 5.0F, effect(MobEffects.NAUSEA, 8, 0));
    // Highly addictive, and it takes as much out of you as it puts in.
    public static final IRegistryObject<Item> SODA_SLURM = soda("soda_slurm", 3.0F, effect(MobEffects.SPEED, 10, 1), effect(MobEffects.HUNGER, 20, 0));
    // Spiked orange is the one you hand to someone you have a grudge against.
    public static final IRegistryObject<Item> SODA_SPIKED_ORANGE = soda("soda_spiked_orange", -8.0F, effect(MobEffects.POISON, 8, 0));

    /**
     * Drinking it leaves the bowl. Hot chocolate also sets a crafting remainder on top of this:
     * usingConvertsTo only covers being drunk, and the chocolate mould takes the bowl from a
     * recipe-shaped path rather than from an eating animation.
     */
    private static Item.Properties drinkable(Item.Properties props) {
        return props.component(DataComponents.CONSUMABLE, Consumables.defaultDrink().build()).usingConvertsTo(Items.BOWL);
    }

    private static Consumable queasy(int durationTicks, float probability) {
        return Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, durationTicks, 0), probability)).build();
    }

    /** Applied rather than eaten, so it takes a moment and can be interrupted. */
    private static IRegistryObject<Item> healing(String name, float healAmount, int stackSize) {
        return register(name, props -> new HealingItem(healAmount, props.stacksTo(stackSize).component(DataComponents.CONSUMABLE, HealingItem.APPLYING)));
    }

    private static IRegistryObject<Item> rawPie(String name) {
        return food(name, 2, 0.3F, RAW_PASTRY);
    }

    /** One certainty of drinking it, so the tooltip lists the effect without the item saying so. */
    private static ConsumeEffect effect(Holder<MobEffect> effect, int seconds, int amplifier) {
        return new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, seconds * 20, amplifier));
    }

    private static IRegistryObject<Item> soda(String name, float healAmount, ConsumeEffect... effects) {
        Consumable.Builder drink = Consumables.defaultDrink().animation(ItemUseAnimation.DRINK);
        for (ConsumeEffect effect : effects) {
            drink.onConsume(effect);
        }

        return register(name, props -> new SodaItem(healAmount, props.stacksTo(16).component(DataComponents.CONSUMABLE, drink.build())));
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
        return CuisineBlocks.ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
