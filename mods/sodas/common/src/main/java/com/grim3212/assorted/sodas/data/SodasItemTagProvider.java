package com.grim3212.assorted.sodas.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.sodas.api.SodasTags;
import com.grim3212.assorted.sodas.common.item.SodasItems;
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

public class SodasItemTagProvider extends LibItemTagProvider {

    public SodasItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // See the other mods' providers: TagAppender only accepts ResourceKeys now.
        Function<TagKey<Item>, ItemTagger> tagger = (tag) -> new ItemTagger(appender.apply(tag));

        SodasAdvancements.rootTags(appender);

        // The broad tag, so a recipe elsewhere asking for any drink finds these.
        tagger.apply(SodasTags.Items.DRINKS)
                .add(SodasItems.SODA_CARBONATED_WATER.get(), SodasItems.SODA_APPLE.get(),
                        SodasItems.SODA_GOLDEN_APPLE.get(), SodasItems.SODA_DIAMOND.get(),
                        SodasItems.SODA_COCOA.get(), SodasItems.SODA_ORANGE.get(),
                        SodasItems.SODA_CREAM_ORANGE.get(), SodasItems.SODA_SPIKED_ORANGE.get(),
                        SodasItems.SODA_ROOT_BEER.get(), SodasItems.SODA_MUSHROOM.get(),
                        SodasItems.SODA_SLURM.get());

        tagger.apply(SodasTags.Items.SODAS)
                .add(SodasItems.SODA_BOTTLE.get())
                .add(SodasItems.SODA_CO2.get())
                .add(SodasItems.SODA_CARBONATED_WATER.get())
                .add(SodasItems.SODA_APPLE.get())
                .add(SodasItems.SODA_GOLDEN_APPLE.get())
                .add(SodasItems.SODA_DIAMOND.get())
                .add(SodasItems.SODA_COCOA.get())
                .add(SodasItems.SODA_ORANGE.get())
                .add(SodasItems.SODA_CREAM_ORANGE.get())
                .add(SodasItems.SODA_SPIKED_ORANGE.get())
                .add(SodasItems.SODA_ROOT_BEER.get())
                .add(SodasItems.SODA_MUSHROOM.get())
                .add(SodasItems.SODA_SLURM.get());
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
