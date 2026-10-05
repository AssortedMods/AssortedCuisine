package com.grim3212.assorted.health.data;

import com.grim3212.assorted.health.api.HealthTags;
import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HealthItemTagProvider extends LibItemTagProvider {

    public HealthItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // See the other mods' providers: TagAppender only accepts ResourceKeys now.
        Function<TagKey<Item>, ItemTagger> tagger = (tag) -> new ItemTagger(appender.apply(tag));

        HealthAdvancements.rootTags(appender);

        // The broad tags, so a recipe elsewhere asking for any food finds these.
        tagger.apply(HealthTags.Items.FOODS).add(HealthItems.SWEETS.get(), HealthItems.POWERED_SWEETS.get());
        tagger.apply(HealthTags.Items.FOODS_CANDY).add(HealthItems.SWEETS.get(), HealthItems.POWERED_SWEETS.get());
    }

    private record ItemTagger(TagAppender<Item> appender) {

        ItemTagger add(Item... items) {
            for (Item item : items) {
                this.appender.add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
            }

            return this;
        }
    }
}
