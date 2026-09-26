package com.grim3212.assorted.kitchen.common.block.blockentity;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.Family;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class KitchenBlockEntityTypes {

    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistryProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    /** One type for all three machines: they store the same thing and only tick at different rates. */
    public static final IRegistryObject<BlockEntityType<KitchenMachineBlockEntity>> MACHINE = BLOCK_ENTITIES.register("machine",
            () -> Services.PLATFORM.createBlockEntityType(KitchenMachineBlockEntity::new,
                    KitchenBlocks.CHEESE_MAKER.get(), KitchenBlocks.BUTTER_CHURN.get(), KitchenBlocks.CHOCOLATE_BAR_MOULD.get()));

    public static void init() {
    }
}
