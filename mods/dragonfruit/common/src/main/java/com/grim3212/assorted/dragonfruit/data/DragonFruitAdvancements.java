package com.grim3212.assorted.dragonfruit.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.Family;
import com.grim3212.assorted.dragonfruit.api.DragonFruitTags;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
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
 * This part's advancement, under the Assorted Cuisine root that every part with advancements writes the same. The
 * root names only common tags, so it loads whichever parts are installed and opens on any of their food or drink.
 */
public class DragonFruitAdvancements implements AdvancementSubProvider {

    private HolderGetter<Item> items;

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> out) {
        this.items = registries.lookupOrThrow(Registries.ITEM);

        // Every part writes this root the same; its icon is swapped for the first of the family's that is installed.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(DragonFruitItems.DRAGON_FRUIT.get(), Component.translatable("advancements." + Family.ID + ".root.title"),
                        Component.translatable("advancements." + Family.ID + ".root.description"),
                        Identifier.withDefaultNamespace("block/pumpkin_top"), AdvancementType.TASK, false, false, false)
                .addCriterion("food", hasTag(DragonFruitTags.Items.FOODS))
                .addCriterion("drink", hasTag(DragonFruitTags.Items.DRINKS))
                .addCriterion("tool", hasTag(DragonFruitTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(out, Identifier.fromNamespaceAndPath(Family.ID, "root").toString());

        task("dragon_fruit", root, DragonFruitItems.DRAGON_FRUIT.get())
                .addCriterion("has_dragon_fruit", has(DragonFruitItems.DRAGON_FRUIT.get()))
                .save(out, id("dragon_fruit"));
    }

    /** Every part with advancements declares all three of the root's tags, empty or not, so none is ever missing. */
    static void rootTags(Function<TagKey<Item>, TagAppender<Item>> appender) {
        appender.apply(DragonFruitTags.Items.FOODS);
        appender.apply(DragonFruitTags.Items.DRINKS);
        appender.apply(DragonFruitTags.Items.KNIVES);
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
