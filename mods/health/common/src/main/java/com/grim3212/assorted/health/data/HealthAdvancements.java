package com.grim3212.assorted.health.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.api.HealthTags;
import com.grim3212.assorted.health.common.item.HealthItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
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
public class HealthAdvancements implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        // Every part writes this root the same; its icon is swapped for the first of the family's that is installed.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(HealthItems.HEALTHPACK.get()), Component.translatable("advancements." + Constants.FAMILY_ID + ".root.title"),
                        Component.translatable("advancements." + Constants.FAMILY_ID + ".root.description"),
                        Optional.of(ResourceLocation.withDefaultNamespace("textures/block/pumpkin_top.png")), AdvancementType.TASK, false, false, false))
                .addCriterion("food", hasTag(HealthTags.Items.FOODS))
                .addCriterion("drink", hasTag(HealthTags.Items.DRINKS))
                .addCriterion("tool", hasTag(HealthTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(output, ResourceLocation.fromNamespaceAndPath(Constants.FAMILY_ID, "root").toString());

        task("field_medic", root, HealthItems.HEALTHPACK_SUPER.get())
                .addCriterion("has_healthpack_super", has(HealthItems.HEALTHPACK_SUPER.get()))
                .save(output, id("field_medic"));
    }

    /** Every part with advancements declares all three of the root's tags, empty or not, so none is ever missing. */
    static void rootTags(Consumer<TagKey<Item>> declare) {
        declare.accept(HealthTags.Items.FOODS);
        declare.accept(HealthTags.Items.DRINKS);
        declare.accept(HealthTags.Items.KNIVES);
    }

    private static Advancement.Builder task(String name, AdvancementHolder parent, ItemLike icon) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, title(name), description(name), null, AdvancementType.TASK, true, true, false);
    }

    private static Criterion<?> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static Criterion<?> hasTag(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag));
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
