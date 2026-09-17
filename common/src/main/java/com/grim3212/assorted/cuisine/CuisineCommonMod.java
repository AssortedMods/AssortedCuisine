package com.grim3212.assorted.cuisine;

import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.block.blockentity.CuisineBlockEntityTypes;
import com.grim3212.assorted.cuisine.common.crafting.CuisineConditions;
import com.grim3212.assorted.cuisine.common.crafting.CuisineRecipeTypes;
import com.grim3212.assorted.cuisine.common.handlers.CuisineCreativeItems;
import com.grim3212.assorted.cuisine.common.handlers.DragonFruitHarvest;
import com.grim3212.assorted.cuisine.common.handlers.LootTableHandlers;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.cuisine.config.CuisineCommonConfig;
import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.lib.events.UseBlockEvent;
import com.grim3212.assorted.lib.platform.Services;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class CuisineCommonMod {

    public static final CuisineCommonConfig COMMON_CONFIG = new CuisineCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        CuisineBlocks.init();
        CuisineItems.init();
        CuisineBlockEntityTypes.init();
        CuisineRecipeTypes.init();
        CuisineConditions.init();
        CuisineCreativeItems.init();

        Services.EVENTS.registerEvent(LootTableModifyEvent.class, (final LootTableModifyEvent event) -> LootTableHandlers.init(event));
        Services.EVENTS.registerEvent(UseBlockEvent.class, (final UseBlockEvent event) -> DragonFruitHarvest.init(event));
    }
}
