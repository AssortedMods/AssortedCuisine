package com.grim3212.assorted.sodas.api;

import com.grim3212.assorted.sodas.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** The tags this mod defines and the common ones it fills or the shared advancement root names. */
public class SodasTags {

    public static class Items {

        /** Anything that can slice. Nothing here is one; the shared advancement root opens on it. */
        public static final TagKey<Item> KNIVES = commonTag("tools/knife");
        /** Nothing here is a food either, but the root opens on one too. */
        public static final TagKey<Item> FOODS = commonTag("foods");
        /** The broad tag other mods look in, so a recipe elsewhere asking for any drink finds these. */
        public static final TagKey<Item> DRINKS = commonTag("drinks");

        /** Every soda, drinkable or not. */
        public static final TagKey<Item> SODAS = modTag("sodas");

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Item> modTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }
}
