package com.grim3212.assorted.cuisine.api;

import com.grim3212.assorted.cuisine.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * The tags this mod defines and the common ones it fills. Recipes take tags rather than items
 * wherever the 1.12 version took an ore dictionary name, so another mod's cheese works here too.
 */
public class CuisineTags {

    public static class Items {

        /** Anything that can slice - the knife, or another mod's equivalent. */
        public static final TagKey<Item> KNIVES = commonTag("tools/knife");
        /** Anything that can whisk - the mixer, or another mod's equivalent. */
        public static final TagKey<Item> MIXERS = commonTag("tools/mixer");

        public static final TagKey<Item> FOODS_CHEESE = commonTag("foods/cheese");
        public static final TagKey<Item> FOODS_BUTTER = commonTag("foods/butter");
        public static final TagKey<Item> FOODS_DOUGH = commonTag("foods/dough");
        public static final TagKey<Item> FOODS_COOKED_EGG = commonTag("foods/cooked_egg");
        public static final TagKey<Item> FOODS_BREAD = commonTag("foods/bread");

        /** The five baked pies, so a datapack can treat them as one thing. */
        public static final TagKey<Item> PIES = modTag("pies");
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
