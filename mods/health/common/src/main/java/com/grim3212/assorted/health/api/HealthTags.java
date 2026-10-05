package com.grim3212.assorted.health.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** The common tags this mod fills, or that the shared advancement root names. */
public class HealthTags {

    public static class Items {

        /** Anything that can slice. Nothing here is one; the shared advancement root opens on it. */
        public static final TagKey<Item> KNIVES = commonTag("tools/knife");
        /** Nothing here is a drink either, but the root opens on one too. */
        public static final TagKey<Item> DRINKS = commonTag("drinks");

        /**
         * The broad tags other mods look in. Nothing in Assorted Cuisine was in any of them before, so a
         * recipe elsewhere asking for "any food" could not see a single item here.
         */
        public static final TagKey<Item> FOODS = commonTag("foods");
        public static final TagKey<Item> FOODS_CANDY = commonTag("foods/candy");

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", name));
        }
    }
}
