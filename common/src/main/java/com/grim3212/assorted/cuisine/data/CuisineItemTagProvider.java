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
        tagger.apply(CuisineTags.Items.WHISKS).add(CuisineItems.WHISK.get());

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

        // The broad tags, so a recipe elsewhere asking for any food or any drink finds these.
        tagger.apply(CuisineTags.Items.FOODS)
                .add(CuisineItems.BUTTER.get(), CuisineItems.CHEESE.get(), CuisineItems.BREAD_SLICE.get(),
                        CuisineItems.CHEESE_BURGER.get(), CuisineItems.HOT_CHEESE.get(), CuisineItems.EGGS_UNMIXED.get(),
                        CuisineItems.EGGS_MIXED.get(), CuisineItems.EGGS_COOKED.get(), CuisineItems.DOUGH.get(),
                        CuisineItems.PUMPKIN_SLICE.get(), CuisineItems.CHOCOLATE_BALL.get(), CuisineItems.CHOCOLATE_BAR.get(),
                        CuisineItems.CHOCOLATE_BAR_WRAPPED.get(), CuisineItems.SWEETS.get(), CuisineItems.POWERED_SWEETS.get(),
                        CuisineItems.DRAGON_FRUIT.get(), CuisineItems.RAW_EMPTY_PIE.get(), CuisineItems.RAW_APPLE_PIE.get(),
                        CuisineItems.RAW_MELON_PIE.get(), CuisineItems.RAW_PUMPKIN_PIE.get(), CuisineItems.RAW_CHOCOLATE_PIE.get(),
                        CuisineItems.RAW_PORK_PIE.get())
                .add(CuisineBlocks.APPLE_PIE.get().asItem(), CuisineBlocks.MELON_PIE.get().asItem(),
                        CuisineBlocks.PUMPKIN_PIE.get().asItem(), CuisineBlocks.CHOCOLATE_PIE.get().asItem(),
                        CuisineBlocks.PORK_PIE.get().asItem(), CuisineBlocks.CHOCOLATE_CAKE.get().asItem());

        tagger.apply(CuisineTags.Items.FOODS_PIE)
                .add(CuisineBlocks.APPLE_PIE.get().asItem(), CuisineBlocks.MELON_PIE.get().asItem(),
                        CuisineBlocks.PUMPKIN_PIE.get().asItem(), CuisineBlocks.CHOCOLATE_PIE.get().asItem(),
                        CuisineBlocks.PORK_PIE.get().asItem());

        tagger.apply(CuisineTags.Items.FOODS_CANDY)
                .add(CuisineItems.SWEETS.get(), CuisineItems.POWERED_SWEETS.get(), CuisineItems.CHOCOLATE_BALL.get(),
                        CuisineItems.CHOCOLATE_BAR.get(), CuisineItems.CHOCOLATE_BAR_WRAPPED.get());

        tagger.apply(CuisineTags.Items.FOODS_FRUIT).add(CuisineItems.DRAGON_FRUIT.get());

        // The two bowls are drinks as much as the bottles are.
        tagger.apply(CuisineTags.Items.DRINKS)
                .add(CuisineItems.CHOCOLATE_BOWL.get(), CuisineItems.HOT_CHOCOLATE.get(),
                        CuisineItems.SODA_CARBONATED_WATER.get(), CuisineItems.SODA_APPLE.get(),
                        CuisineItems.SODA_GOLDEN_APPLE.get(), CuisineItems.SODA_DIAMOND.get(),
                        CuisineItems.SODA_COCOA.get(), CuisineItems.SODA_ORANGE.get(),
                        CuisineItems.SODA_CREAM_ORANGE.get(), CuisineItems.SODA_SPIKED_ORANGE.get(),
                        CuisineItems.SODA_ROOT_BEER.get(), CuisineItems.SODA_MUSHROOM.get(),
                        CuisineItems.SODA_SLURM.get());

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
