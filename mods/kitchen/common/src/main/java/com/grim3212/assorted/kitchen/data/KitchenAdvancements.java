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
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

/**
 * This part's dairy, chocolate and pie branches, under the Assorted Cuisine root that every part with advancements
 * writes the same. The root names only common tags, so it loads whichever parts are installed.
 */
public class KitchenAdvancements extends AdvancementSubProvider {

    private final HolderGetter<Item> items;

    public KitchenAdvancements(BootstrapContext<Advancement> output) {
        super(output);
        this.items = output.lookup(Registries.ITEM);
    }

    @Override
    public void generate() {
        // Every part writes this root the same but for the placeholder icon, which AdvancementIcons swaps at load.
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStackTemplate(KitchenItems.CHEESE_BURGER.get().asItem()), Component.translatable("advancements." + Constants.FAMILY_ID + ".root.title"),
                        Component.translatable("advancements." + Constants.FAMILY_ID + ".root.description"),
                        Optional.of(new ClientAsset.ResourceTexture(Identifier.withDefaultNamespace("block/pumpkin_top"))), AdvancementType.TASK, false, false, false))
                // Any one of the three is enough to open the tab, so no branch is a prerequisite
                // for seeing the rest.
                .addCriterion("food", hasTag(KitchenTags.Items.FOODS))
                .addCriterion("drink", hasTag(KitchenTags.Items.DRINKS))
                .addCriterion("tool", hasTag(KitchenTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(this.output, Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "root").toString());

        this.dairy(root);
        this.chocolate(root);
        this.pies(root);
    }

    private void dairy(AdvancementHolder root) {
        AdvancementHolder cheese = task("cheese", root, KitchenBlocks.CHEESE_BLOCK.get())
                .addCriterion("has_cheese_block", has(KitchenBlocks.CHEESE_BLOCK.get()))
                .save(this.output, id("cheese"));

        task("butter", root, KitchenItems.BUTTER.get())
                .addCriterion("has_butter", has(KitchenItems.BUTTER.get()))
                .save(this.output, id("butter"));

        task("sandwich", cheese, KitchenItems.CHEESE_BURGER.get())
                .addCriterion("has_cheese_burger", has(KitchenItems.CHEESE_BURGER.get()))
                .save(this.output, id("sandwich"));
    }

    private void chocolate(AdvancementHolder root) {
        AdvancementHolder bar = task("chocolate", root, KitchenItems.CHOCOLATE_BAR.get())
                .addCriterion("has_chocolate_bar", has(KitchenItems.CHOCOLATE_BAR.get()))
                .save(this.output, id("chocolate"));

        goal("chocolate_cake", bar, KitchenBlocks.CHOCOLATE_CAKE.get())
                .addCriterion("has_chocolate_cake", has(KitchenBlocks.CHOCOLATE_CAKE.get()))
                .save(this.output, id("chocolate_cake"));
    }

    private void pies(AdvancementHolder root) {
        AdvancementHolder pie = task("pie", root, KitchenBlocks.APPLE_PIE.get())
                .addCriterion("has_pie", hasTag(KitchenTags.Items.PIES))
                .save(this.output, id("pie"));

        // Every criterion has to be met, which is the default strategy: all five pies.
        Advancement.Builder every = challenge("every_pie", pie, KitchenBlocks.CHOCOLATE_PIE.get());
        for (ItemLike baked : new ItemLike[]{KitchenBlocks.APPLE_PIE.get(), KitchenBlocks.MELON_PIE.get(),
                KitchenBlocks.PUMPKIN_PIE.get(), KitchenBlocks.CHOCOLATE_PIE.get(), KitchenBlocks.PORK_PIE.get()}) {
            every.addCriterion("has_" + name(baked), has(baked));
        }
        every.save(this.output, id("every_pie"));
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
                .display(icon.asItem(), title(name), description(name), type, true, true, false);
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
