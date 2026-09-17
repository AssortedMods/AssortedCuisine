package com.grim3212.assorted.cuisine.common.block.blockentity;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CuisineBlockEntityTypes {

    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    /** One type for all three machines: they store the same thing and only tick at different rates. */
    public static final IRegistryObject<BlockEntityType<CuisineMachineBlockEntity>> MACHINE = BLOCK_ENTITIES.register("machine",
            () -> Services.PLATFORM.createBlockEntityType(CuisineMachineBlockEntity::new,
                    CuisineBlocks.CHEESE_MAKER.get(), CuisineBlocks.BUTTER_CHURN.get(), CuisineBlocks.CHOCOLATE_BAR_MOULD.get()));

    public static void init() {
    }
}
