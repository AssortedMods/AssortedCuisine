package com.grim3212.assorted.kitchen.data;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.api.KitchenTags;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * This part's dairy, chocolate and pie branches, under the Assorted Cuisine root that every part with advancements
 * writes the same. The root names only common tags, so it loads whichever parts are installed.
 */
public class KitchenAdvancements implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        // Every part writes this root the same but for the placeholder icon, which AdvancementIcons swaps at load.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(KitchenItems.CHEESE_BURGER.get()), Component.translatable("advancements." + Constants.FAMILY_ID + ".root.title"),
                        Component.translatable("advancements." + Constants.FAMILY_ID + ".root.description"),
                        Optional.of(ResourceLocation.withDefaultNamespace("textures/block/pumpkin_top.png")), AdvancementType.TASK, false, false, false))
                // Any one of the three is enough to open the tab, so no branch is a prerequisite
                // for seeing the rest.
                .addCriterion("food", hasTag(KitchenTags.Items.FOODS))
                .addCriterion("drink", hasTag(KitchenTags.Items.DRINKS))
                .addCriterion("tool", hasTag(KitchenTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(output, ResourceLocation.fromNamespaceAndPath(Constants.FAMILY_ID, "root").toString());

        dairy(root, output);
        chocolate(root, output);
        pies(root, output);
    }

    private static void dairy(AdvancementHolder root, Consumer<AdvancementHolder> output) {
        AdvancementHolder cheese = task("cheese", root, KitchenBlocks.CHEESE_BLOCK.get())
                .addCriterion("has_cheese_block", has(KitchenBlocks.CHEESE_BLOCK.get()))
                .save(output, id("cheese"));

        task("butter", root, KitchenItems.BUTTER.get())
                .addCriterion("has_butter", has(KitchenItems.BUTTER.get()))
                .save(output, id("butter"));

        task("sandwich", cheese, KitchenItems.CHEESE_BURGER.get())
                .addCriterion("has_cheese_burger", has(KitchenItems.CHEESE_BURGER.get()))
                .save(output, id("sandwich"));
    }

    private static void chocolate(AdvancementHolder root, Consumer<AdvancementHolder> output) {
        AdvancementHolder bar = task("chocolate", root, KitchenItems.CHOCOLATE_BAR.get())
                .addCriterion("has_chocolate_bar", has(KitchenItems.CHOCOLATE_BAR.get()))
                .save(output, id("chocolate"));

        goal("chocolate_cake", bar, KitchenBlocks.CHOCOLATE_CAKE.get())
                .addCriterion("has_chocolate_cake", has(KitchenBlocks.CHOCOLATE_CAKE.get()))
                .save(output, id("chocolate_cake"));
    }

    private static void pies(AdvancementHolder root, Consumer<AdvancementHolder> output) {
        AdvancementHolder pie = task("pie", root, KitchenBlocks.APPLE_PIE.get())
                .addCriterion("has_pie", hasTag(KitchenTags.Items.PIES))
                .save(output, id("pie"));

        // Every criterion has to be met, which is the default strategy: all five pies.
        Advancement.Builder every = challenge("every_pie", pie, KitchenBlocks.CHOCOLATE_PIE.get());
        for (ItemLike baked : new ItemLike[]{KitchenBlocks.APPLE_PIE.get(), KitchenBlocks.MELON_PIE.get(),
                KitchenBlocks.PUMPKIN_PIE.get(), KitchenBlocks.CHOCOLATE_PIE.get(), KitchenBlocks.PORK_PIE.get()}) {
            every.addCriterion("has_" + name(baked), has(baked));
        }
        every.save(output, id("every_pie"));
    }

    private static Advancement.Builder task(String name, AdvancementHolder parent, ItemLike icon) {
        return builder(name, parent, icon, AdvancementType.TASK);
    }

    private static Advancement.Builder goal(String name, AdvancementHolder parent, ItemLike icon) {
        return builder(name, parent, icon, AdvancementType.GOAL);
    }

    private static Advancement.Builder challenge(String name, AdvancementHolder parent, ItemLike icon) {
        return builder(name, parent, icon, AdvancementType.CHALLENGE);
    }

    private static Advancement.Builder builder(String name, AdvancementHolder parent, ItemLike icon, AdvancementType type) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, title(name), description(name), null, type, true, true, false);
    }

    private static Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static Criterion<?> hasTag(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag));
    }

    private static String name(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    private static Component title(String name) {
        return Component.translatable("advancements." + Constants.MOD_ID + "." + name + ".title");
    }

    private static Component description(String name) {
        return Component.translatable("advancements." + Constants.MOD_ID + "." + name + ".description");
    }

    private static String id(String name) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name).toString();
    }
}
