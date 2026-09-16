package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CuisineBlockLoot extends LibBlockLootProvider {

    public CuisineBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> CuisineBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        this.dropSelf(CuisineBlocks.CHEESE_BLOCK.get());
        this.dropSelf(CuisineBlocks.CHEESE_MAKER.get());
        this.dropSelf(CuisineBlocks.BUTTER_CHURN.get());
        this.dropSelf(CuisineBlocks.CHOCOLATE_BAR_MOULD.get());
        this.dropSelf(CuisineBlocks.CHOCOLATE_BLOCK.get());

        // Cake and pies drop nothing once placed, like vanilla cake.
        this.add(CuisineBlocks.CHOCOLATE_CAKE.get(), noDrop());
        for (Block pie : new Block[]{CuisineBlocks.APPLE_PIE.get(), CuisineBlocks.MELON_PIE.get(), CuisineBlocks.PUMPKIN_PIE.get(), CuisineBlocks.CHOCOLATE_PIE.get(), CuisineBlocks.PORK_PIE.get()}) {
            this.add(pie, noDrop());
        }
    }
}
