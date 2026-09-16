package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.api.CuisineTags;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CuisineItemTagProvider extends LibItemTagProvider {

    public CuisineItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // See the other mods' providers: TagAppender only accepts ResourceKeys now.
        Function<TagKey<Item>, ItemTagger> tagger = (tag) -> new ItemTagger(appender.apply(tag));

        tagger.apply(CuisineTags.Items.KNIVES).add(CuisineItems.KNIFE.get());
        tagger.apply(CuisineTags.Items.MIXERS).add(CuisineItems.MIXER.get());

        tagger.apply(CuisineTags.Items.FOODS_CHEESE).add(CuisineItems.CHEESE.get());
        tagger.apply(CuisineTags.Items.FOODS_BUTTER).add(CuisineItems.BUTTER.get());
        tagger.apply(CuisineTags.Items.FOODS_DOUGH).add(CuisineItems.DOUGH.get());
        tagger.apply(CuisineTags.Items.FOODS_COOKED_EGG).add(CuisineItems.EGGS_COOKED.get());

        // 1.12 registered the bread slice under the "bread" ore dictionary name, so sandwiches
        // could be built from either. The tag carries vanilla bread for the same reason.
        tagger.apply(CuisineTags.Items.FOODS_BREAD).add(Items.BREAD).add(CuisineItems.BREAD_SLICE.get());

        tagger.apply(CuisineTags.Items.PIES)
                .add(CuisineBlocks.APPLE_PIE.get().asItem())
                .add(CuisineBlocks.MELON_PIE.get().asItem())
                .add(CuisineBlocks.PUMPKIN_PIE.get().asItem())
                .add(CuisineBlocks.CHOCOLATE_PIE.get().asItem())
                .add(CuisineBlocks.PORK_PIE.get().asItem());

        tagger.apply(CuisineTags.Items.SODAS)
                .add(CuisineItems.SODA_BOTTLE.get())
                .add(CuisineItems.SODA_CO2.get())
                .add(CuisineItems.SODA_CARBONATED_WATER.get())
                .add(CuisineItems.SODA_APPLE.get())
                .add(CuisineItems.SODA_GOLDEN_APPLE.get())
                .add(CuisineItems.SODA_DIAMOND.get())
                .add(CuisineItems.SODA_COCOA.get())
                .add(CuisineItems.SODA_ORANGE.get())
                .add(CuisineItems.SODA_CREAM_ORANGE.get())
                .add(CuisineItems.SODA_SPIKED_ORANGE.get())
                .add(CuisineItems.SODA_ROOT_BEER.get())
                .add(CuisineItems.SODA_MUSHROOM.get())
                .add(CuisineItems.SODA_SLURM.get());
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
