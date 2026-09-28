package com.grim3212.assorted.sodas.data;

import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.api.SodasTags;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This part's advancements, under the Assorted Cuisine root that every part with advancements writes the same. The
 * root names only common tags, so it loads whichever parts are installed and opens on any of their food or drink.
 */
public class SodasAdvancements implements AdvancementSubProvider {

    private HolderGetter<Item> items;

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> out) {
        this.items = registries.lookupOrThrow(Registries.ITEM);

        // Every part writes this root the same; its icon is swapped for the first of the family's that is installed.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(SodasItems.SODA_ORANGE.get(), Component.translatable("advancements." + Constants.FAMILY_ID + ".root.title"),
                        Component.translatable("advancements." + Constants.FAMILY_ID + ".root.description"),
                        Identifier.withDefaultNamespace("block/pumpkin_top"), AdvancementType.TASK, false, false, false)
                .addCriterion("food", hasTag(SodasTags.Items.FOODS))
                .addCriterion("drink", hasTag(SodasTags.Items.DRINKS))
                .addCriterion("tool", hasTag(SodasTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(out, Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "root").toString());

        AdvancementHolder soda = task("soda", root, SodasItems.SODA_CARBONATED_WATER.get())
                .addCriterion("has_soda", hasTag(SodasTags.Items.SODAS))
                .save(out, id("soda"));

        Advancement.Builder every = challenge("every_soda", soda, SodasItems.SODA_GOLDEN_APPLE.get());
        for (ItemLike flavour : new ItemLike[]{SodasItems.SODA_CARBONATED_WATER.get(), SodasItems.SODA_APPLE.get(),
                SodasItems.SODA_GOLDEN_APPLE.get(), SodasItems.SODA_DIAMOND.get(), SodasItems.SODA_COCOA.get(),
                SodasItems.SODA_ORANGE.get(), SodasItems.SODA_CREAM_ORANGE.get(), SodasItems.SODA_ROOT_BEER.get(),
                SodasItems.SODA_MUSHROOM.get(), SodasItems.SODA_SLURM.get()}) {
            every.addCriterion("has_" + name(flavour), has(flavour));
        }
        every.save(out, id("every_soda"));

        // Drunk, not merely held: the whole point of this one is what it does to you.
        goal("spiked", soda, SodasItems.SODA_SPIKED_ORANGE.get())
                .addCriterion("drank_spiked_orange", ConsumeItemTrigger.TriggerInstance.usedItem(this.items, SodasItems.SODA_SPIKED_ORANGE.get()))
                .save(out, id("spiked"));
    }

    /** Every part with advancements declares all three of the root's tags, empty or not, so none is ever missing. */
    static void rootTags(Function<TagKey<Item>, TagAppender<Item>> appender) {
        appender.apply(SodasTags.Items.FOODS);
        appender.apply(SodasTags.Items.DRINKS);
        appender.apply(SodasTags.Items.KNIVES);
    }

    private Advancement.Builder task(String name, AdvancementHolder parent, ItemLike icon) {
        return builder(name, parent, icon, AdvancementType.TASK);
    }

    private Advancement.Builder goal(String name, AdvancementHolder parent, ItemLike icon) {
        return builder(name, parent, icon, AdvancementType.GOAL);
    }

    private Advancement.Builder challenge(String name, AdvancementHolder parent, ItemLike icon) {
        return builder(name, parent, icon, AdvancementType.CHALLENGE);
    }

    private Advancement.Builder builder(String name, AdvancementHolder parent, ItemLike icon, AdvancementType type) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, title(name), description(name), null, type, true, true, false);
    }

    private Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private Criterion<?> hasTag(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(this.items, tag));
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
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, name).toString();
    }
}
