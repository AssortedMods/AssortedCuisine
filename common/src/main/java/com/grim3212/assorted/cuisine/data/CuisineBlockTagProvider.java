package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CuisineBlockTagProvider extends LibBlockTagProvider {

    public CuisineBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> appender) {
        // See the other mods' providers: TagAppender only accepts ResourceKeys now.
        Function<TagKey<Block>, BlockTagger> tagger = (tag) -> new BlockTagger(appender.apply(tag));

        // The stone-built machines want a pickaxe; everything else here comes off by hand.
        tagger.apply(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CuisineBlocks.CHEESE_MAKER.get())
                .add(CuisineBlocks.CHOCOLATE_BAR_MOULD.get());

        tagger.apply(BlockTags.MINEABLE_WITH_AXE)
                .add(CuisineBlocks.BUTTER_CHURN.get());
    }

    private record BlockTagger(TagAppender<Block> appender) {

        BlockTagger add(Block... blocks) {
            for (Block block : blocks) {
                this.appender.add(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
            }

            return this;
        }
    }
}
