package com.grim3212.assorted.dragonfruit.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** The common tags this mod fills, or that its recipe and the shared advancement root name. */
public class DragonFruitTags {

    public static class Items {

        /** Anything that can slice, the kitchen's knife or another mod's equivalent. */
        public static final TagKey<Item> KNIVES = commonTag("tools/knife");
        /** Nothing here is a drink, but the shared advancement root opens on one. */
        public static final TagKey<Item> DRINKS = commonTag("drinks");

        /**
         * The broad tags other mods look in. Nothing in Assorted Cuisine was in any of them before, so a
         * recipe elsewhere asking for "any food" could not see a single item here.
         */
        public static final TagKey<Item> FOODS = commonTag("foods");
        public static final TagKey<Item> FOODS_FRUIT = commonTag("foods/fruit");

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", name));
        }
    }
}
