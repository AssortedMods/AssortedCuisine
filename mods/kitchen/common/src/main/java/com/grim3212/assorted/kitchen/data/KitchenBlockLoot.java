package com.grim3212.assorted.kitchen.data;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.lib.data.LibBlockLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class KitchenBlockLoot extends LibBlockLootProvider {

    public KitchenBlockLoot(HolderLookup.Provider registries) {
        super(registries, () -> KitchenBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList()));
    }

    @Override
    public void generate() {
        this.dropSelf(KitchenBlocks.CHEESE_BLOCK.get());
        this.dropSelf(KitchenBlocks.CHEESE_MAKER.get());
        this.dropSelf(KitchenBlocks.BUTTER_CHURN.get());
        this.dropSelf(KitchenBlocks.CHOCOLATE_BAR_MOULD.get());
        this.dropSelf(KitchenBlocks.CHOCOLATE_BLOCK.get());

        // Cake and pies drop nothing once placed, like vanilla cake.
        this.add(KitchenBlocks.CHOCOLATE_CAKE.get(), noDrop());
        for (Block pie : new Block[]{KitchenBlocks.APPLE_PIE.get(), KitchenBlocks.MELON_PIE.get(), KitchenBlocks.PUMPKIN_PIE.get(), KitchenBlocks.CHOCOLATE_PIE.get(), KitchenBlocks.PORK_PIE.get()}) {
            this.add(pie, noDrop());
        }
    }
}
