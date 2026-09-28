package com.grim3212.assorted.health;

import com.grim3212.assorted.health.common.handlers.HealthCreativeItems;
import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.AdvancementIcons;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class HealthCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "healthpack"), 20)
                .manualOrder(40);

        HealthItems.init();
        HealthCreativeItems.init();

        // Recipes and advancements unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
        // The advancement root every part shares: its icon is the first installed of the family's.
        AdvancementIcons.register(Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "root"), () -> Families.icons(Constants.FAMILY_ID));
    }
}
