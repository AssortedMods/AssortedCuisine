package com.grim3212.assorted.sodas.data;

import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.api.SodasTags;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
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
 * This part's advancements, under the Assorted Cuisine root that every part with advancements writes the same. The
 * root names only common tags, so it loads whichever parts are installed and opens on any of their food or drink.
 */
public class SodasAdvancements implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        // Every part writes this root the same; its icon is swapped for the first of the family's that is installed.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(SodasItems.SODA_ORANGE.get()), Component.translatable("advancements." + Constants.FAMILY_ID + ".root.title"),
                        Component.translatable("advancements." + Constants.FAMILY_ID + ".root.description"),
                        Optional.of(ResourceLocation.withDefaultNamespace("textures/block/pumpkin_top.png")), AdvancementType.TASK, false, false, false))
                .addCriterion("food", hasTag(SodasTags.Items.FOODS))
                .addCriterion("drink", hasTag(SodasTags.Items.DRINKS))
                .addCriterion("tool", hasTag(SodasTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(output, ResourceLocation.fromNamespaceAndPath(Constants.FAMILY_ID, "root").toString());

        AdvancementHolder soda = task("soda", root, SodasItems.SODA_CARBONATED_WATER.get())
                .addCriterion("has_soda", hasTag(SodasTags.Items.SODAS))
                .save(output, id("soda"));

        Advancement.Builder every = challenge("every_soda", soda, SodasItems.SODA_GOLDEN_APPLE.get());
        for (ItemLike flavor : new ItemLike[]{SodasItems.SODA_CARBONATED_WATER.get(), SodasItems.SODA_APPLE.get(),
                SodasItems.SODA_GOLDEN_APPLE.get(), SodasItems.SODA_DIAMOND.get(), SodasItems.SODA_COCOA.get(),
                SodasItems.SODA_ORANGE.get(), SodasItems.SODA_CREAM_ORANGE.get(), SodasItems.SODA_ROOT_BEER.get(),
                SodasItems.SODA_MUSHROOM.get(), SodasItems.SODA_SLURM.get()}) {
            every.addCriterion("has_" + name(flavor), has(flavor));
        }
        every.save(output, id("every_soda"));

        // Drunk, not merely held: the whole point of this one is what it does to you.
        goal("spiked", soda, SodasItems.SODA_SPIKED_ORANGE.get())
                .addCriterion("drank_spiked_orange", ConsumeItemTrigger.TriggerInstance.usedItem(SodasItems.SODA_SPIKED_ORANGE.get()))
                .save(output, id("spiked"));
    }

    /** Every part with advancements declares all three of the root's tags, empty or not, so none is ever missing. */
    static void rootTags(Consumer<TagKey<Item>> declare) {
        declare.accept(SodasTags.Items.FOODS);
        declare.accept(SodasTags.Items.DRINKS);
        declare.accept(SodasTags.Items.KNIVES);
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
