package com.grim3212.assorted.kitchen;

import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.block.blockentity.KitchenBlockEntityTypes;
import com.grim3212.assorted.kitchen.common.crafting.KitchenRecipeTypes;
import com.grim3212.assorted.kitchen.common.handlers.KitchenCreativeItems;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.lib.migration.AdvancementIcons;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class KitchenCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        KitchenBlocks.init();
        KitchenItems.init();
        KitchenBlockEntityTypes.init();
        KitchenRecipeTypes.init();
        KitchenCreativeItems.init();

        // Recipes and advancements unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
        // The advancement root every part shares: its icon is the first installed of the family's.
        AdvancementIcons.register(Identifier.fromNamespaceAndPath(Family.ID, "root"), Family.ICONS);
    }
}
