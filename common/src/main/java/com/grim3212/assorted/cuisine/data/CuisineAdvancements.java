package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.api.CuisineTags;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

/**
 * The mod's advancement tab. 1.12 had none, and the port inherited that: until now the only
 * advancements here were the invisible ones the recipe book uses to unlock recipes.
 *
 * <p>Nothing is gated on a part being enabled - advancements take no load conditions - so a tab
 * with a part switched off simply has branches nobody can finish.
 */
public class CuisineAdvancements implements AdvancementSubProvider {

    private HolderGetter<Item> items;

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> out) {
        this.items = registries.lookupOrThrow(Registries.ITEM);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(CuisineItems.CHEESE_BURGER.get(), title("root"), description("root"),
                        Identifier.withDefaultNamespace("block/pumpkin_top"), AdvancementType.TASK, false, false, false)
                // Any one of the three is enough to open the tab, so no branch is a prerequisite
                // for seeing the rest.
                .addCriterion("food", hasTag(CuisineTags.Items.FOODS))
                .addCriterion("drink", hasTag(CuisineTags.Items.DRINKS))
                .addCriterion("tool", hasTag(CuisineTags.Items.KNIVES))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(out, id("root"));

        this.dairy(root, out);
        this.chocolate(root, out);
        this.pies(root, out);
        this.soda(root, out);

        task("dragon_fruit", root, CuisineItems.DRAGON_FRUIT.get())
                .addCriterion("has_dragon_fruit", has(CuisineItems.DRAGON_FRUIT.get()))
                .save(out, id("dragon_fruit"));

        task("field_medic", root, CuisineItems.HEALTHPACK_SUPER.get())
                .addCriterion("has_healthpack_super", has(CuisineItems.HEALTHPACK_SUPER.get()))
                .save(out, id("field_medic"));
    }

    private void dairy(AdvancementHolder root, Consumer<AdvancementHolder> out) {
        AdvancementHolder cheese = task("cheese", root, CuisineBlocks.CHEESE_BLOCK.get())
                .addCriterion("has_cheese_block", has(CuisineBlocks.CHEESE_BLOCK.get()))
                .save(out, id("cheese"));

        task("butter", root, CuisineItems.BUTTER.get())
                .addCriterion("has_butter", has(CuisineItems.BUTTER.get()))
                .save(out, id("butter"));

        task("sandwich", cheese, CuisineItems.CHEESE_BURGER.get())
                .addCriterion("has_cheese_burger", has(CuisineItems.CHEESE_BURGER.get()))
                .save(out, id("sandwich"));
    }

    private void chocolate(AdvancementHolder root, Consumer<AdvancementHolder> out) {
        AdvancementHolder bar = task("chocolate", root, CuisineItems.CHOCOLATE_BAR.get())
                .addCriterion("has_chocolate_bar", has(CuisineItems.CHOCOLATE_BAR.get()))
                .save(out, id("chocolate"));

        goal("chocolate_cake", bar, CuisineBlocks.CHOCOLATE_CAKE.get())
                .addCriterion("has_chocolate_cake", has(CuisineBlocks.CHOCOLATE_CAKE.get()))
                .save(out, id("chocolate_cake"));
    }

    private void pies(AdvancementHolder root, Consumer<AdvancementHolder> out) {
        AdvancementHolder pie = task("pie", root, CuisineBlocks.APPLE_PIE.get())
                .addCriterion("has_pie", hasTag(CuisineTags.Items.PIES))
                .save(out, id("pie"));

        // Every criterion has to be met, which is the default strategy: all five pies.
        Advancement.Builder every = challenge("every_pie", pie, CuisineBlocks.CHOCOLATE_PIE.get());
        for (ItemLike baked : new ItemLike[]{CuisineBlocks.APPLE_PIE.get(), CuisineBlocks.MELON_PIE.get(),
                CuisineBlocks.PUMPKIN_PIE.get(), CuisineBlocks.CHOCOLATE_PIE.get(), CuisineBlocks.PORK_PIE.get()}) {
            every.addCriterion("has_" + name(baked), has(baked));
        }
        every.save(out, id("every_pie"));
    }

    private void soda(AdvancementHolder root, Consumer<AdvancementHolder> out) {
        AdvancementHolder soda = task("soda", root, CuisineItems.SODA_CARBONATED_WATER.get())
                .addCriterion("has_soda", hasTag(CuisineTags.Items.SODAS))
                .save(out, id("soda"));

        Advancement.Builder every = challenge("every_soda", soda, CuisineItems.SODA_GOLDEN_APPLE.get());
        for (ItemLike flavour : new ItemLike[]{CuisineItems.SODA_CARBONATED_WATER.get(), CuisineItems.SODA_APPLE.get(),
                CuisineItems.SODA_GOLDEN_APPLE.get(), CuisineItems.SODA_DIAMOND.get(), CuisineItems.SODA_COCOA.get(),
                CuisineItems.SODA_ORANGE.get(), CuisineItems.SODA_CREAM_ORANGE.get(), CuisineItems.SODA_ROOT_BEER.get(),
                CuisineItems.SODA_MUSHROOM.get(), CuisineItems.SODA_SLURM.get()}) {
            every.addCriterion("has_" + name(flavour), has(flavour));
        }
        every.save(out, id("every_soda"));

        // Drunk, not merely held: the whole point of this one is what it does to you.
        goal("spiked", soda, CuisineItems.SODA_SPIKED_ORANGE.get())
                .addCriterion("drank_spiked_orange", ConsumeItemTrigger.TriggerInstance.usedItem(this.items, CuisineItems.SODA_SPIKED_ORANGE.get()))
                .save(out, id("spiked"));
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
