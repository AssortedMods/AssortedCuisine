package com.grim3212.assorted.health.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.Family;
import com.grim3212.assorted.health.api.HealthTags;
import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
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
public class HealthAdvancements implements AdvancementSubProvider {

    private HolderGetter<Item> items;

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> out) {
        this.items = registries.lookupOrThrow(Registries.ITEM);

        // Every part writes this root the same; its icon is swapped for the first of the family's that is installed.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(HealthItems.HEALTHPACK.get(), Component.translatable("advancements." + Family.ID + ".root.title"),
                        Component.translatable("advancements." + Family.ID + ".root.description"),
                        Identifier.withDefaultNamespace("block/pumpkin_top"), AdvancementType.TASK, false, false, false)
                .addCriterion("food", hasTag(HealthTags.Items.FOODS))
                .addCriterion("drink", hasTag(HealthTags.Items.DRINKS))
                .addCriterion("tool", hasTag(HealthTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(out, Identifier.fromNamespaceAndPath(Family.ID, "root").toString());

        task("field_medic", root, HealthItems.HEALTHPACK_SUPER.get())
                .addCriterion("has_healthpack_super", has(HealthItems.HEALTHPACK_SUPER.get()))
                .save(out, id("field_medic"));
    }

    /** Every part with advancements declares all three of the root's tags, empty or not, so none is ever missing. */
    static void rootTags(Function<TagKey<Item>, TagAppender<Item>> appender) {
        appender.apply(HealthTags.Items.FOODS);
        appender.apply(HealthTags.Items.DRINKS);
        appender.apply(HealthTags.Items.KNIVES);
    }

    private Advancement.Builder task(String name, AdvancementHolder parent, ItemLike icon) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, title(name), description(name), null, AdvancementType.TASK, true, true, false);
    }

    private Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private Criterion<?> hasTag(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(this.items, tag));
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
