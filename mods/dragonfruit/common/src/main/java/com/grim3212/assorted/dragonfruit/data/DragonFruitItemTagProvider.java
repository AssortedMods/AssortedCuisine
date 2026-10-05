package com.grim3212.assorted.dragonfruit.data;

import com.grim3212.assorted.dragonfruit.api.DragonFruitTags;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DragonFruitItemTagProvider extends LibItemTagProvider {

    public DragonFruitItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        DragonFruitAdvancements.rootTags(appender);

        // The broad tags, so a recipe elsewhere asking for any food or any fruit finds it.
        appender.apply(DragonFruitTags.Items.FOODS).add(key(DragonFruitItems.DRAGON_FRUIT.get()));
        appender.apply(DragonFruitTags.Items.FOODS_FRUIT).add(key(DragonFruitItems.DRAGON_FRUIT.get()));
    }

    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }
}
