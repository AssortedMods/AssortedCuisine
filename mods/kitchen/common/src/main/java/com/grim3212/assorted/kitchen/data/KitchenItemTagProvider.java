package com.grim3212.assorted.kitchen.data;

import com.grim3212.assorted.kitchen.api.KitchenTags;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
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

public class KitchenItemTagProvider extends LibItemTagProvider {

    public KitchenItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // See the other mods' providers: TagAppender only accepts ResourceKeys now.
        Function<TagKey<Item>, ItemTagger> tagger = (tag) -> new ItemTagger(appender.apply(tag));

        tagger.apply(KitchenTags.Items.KNIVES).add(KitchenItems.KNIFE.get());
        tagger.apply(KitchenTags.Items.WHISKS).add(KitchenItems.WHISK.get());
        tagger.apply(KitchenTags.Items.MORTARS_AND_PESTLES).add(KitchenItems.MORTAR_AND_PESTLE.get());

        tagger.apply(KitchenTags.Items.FOODS_CHEESE).add(KitchenItems.CHEESE.get());
        tagger.apply(KitchenTags.Items.FOODS_BUTTER).add(KitchenItems.BUTTER.get());
        tagger.apply(KitchenTags.Items.FOODS_DOUGH).add(KitchenItems.DOUGH.get());
        tagger.apply(KitchenTags.Items.FOODS_COOKED_EGG).add(KitchenItems.EGGS_COOKED.get());

        // 1.12 registered the bread slice under the "bread" ore dictionary name, so sandwiches
        // could be built from either. The tag carries vanilla bread for the same reason.
        tagger.apply(KitchenTags.Items.FOODS_BREAD).add(Items.BREAD).add(KitchenItems.BREAD_SLICE.get());

        tagger.apply(KitchenTags.Items.PIES)
                .add(KitchenBlocks.APPLE_PIE.get().asItem())
                .add(KitchenBlocks.MELON_PIE.get().asItem())
                .add(KitchenBlocks.PUMPKIN_PIE.get().asItem())
                .add(KitchenBlocks.CHOCOLATE_PIE.get().asItem())
                .add(KitchenBlocks.PORK_PIE.get().asItem());

        // The broad tags, so a recipe elsewhere asking for any food or any drink finds these.
        tagger.apply(KitchenTags.Items.FOODS)
                .add(KitchenItems.BUTTER.get(), KitchenItems.CHEESE.get(), KitchenItems.BREAD_SLICE.get(),
                        KitchenItems.CHEESE_BURGER.get(), KitchenItems.HOT_CHEESE.get(), KitchenItems.EGGS_UNMIXED.get(),
                        KitchenItems.EGGS_MIXED.get(), KitchenItems.EGGS_COOKED.get(), KitchenItems.DOUGH.get(),
                        KitchenItems.PUMPKIN_SLICE.get(), KitchenItems.CHOCOLATE_BALL.get(), KitchenItems.CHOCOLATE_BAR.get(),
                        KitchenItems.CHOCOLATE_BAR_WRAPPED.get(), KitchenItems.RAW_EMPTY_PIE.get(), KitchenItems.RAW_APPLE_PIE.get(),
                        KitchenItems.RAW_MELON_PIE.get(), KitchenItems.RAW_PUMPKIN_PIE.get(), KitchenItems.RAW_CHOCOLATE_PIE.get(),
                        KitchenItems.RAW_PORK_PIE.get())
                .add(KitchenBlocks.APPLE_PIE.get().asItem(), KitchenBlocks.MELON_PIE.get().asItem(),
                        KitchenBlocks.PUMPKIN_PIE.get().asItem(), KitchenBlocks.CHOCOLATE_PIE.get().asItem(),
                        KitchenBlocks.PORK_PIE.get().asItem(), KitchenBlocks.CHOCOLATE_CAKE.get().asItem());

        tagger.apply(KitchenTags.Items.FOODS_PIE)
                .add(KitchenBlocks.APPLE_PIE.get().asItem(), KitchenBlocks.MELON_PIE.get().asItem(),
                        KitchenBlocks.PUMPKIN_PIE.get().asItem(), KitchenBlocks.CHOCOLATE_PIE.get().asItem(),
                        KitchenBlocks.PORK_PIE.get().asItem());

        tagger.apply(KitchenTags.Items.FOODS_CANDY)
                .add(KitchenItems.CHOCOLATE_BALL.get(), KitchenItems.CHOCOLATE_BAR.get(), KitchenItems.CHOCOLATE_BAR_WRAPPED.get());

        // The two bowls are drinks as much as a bottle of soda is.
        tagger.apply(KitchenTags.Items.DRINKS)
                .add(KitchenItems.CHOCOLATE_BOWL.get(), KitchenItems.HOT_CHOCOLATE.get());
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
