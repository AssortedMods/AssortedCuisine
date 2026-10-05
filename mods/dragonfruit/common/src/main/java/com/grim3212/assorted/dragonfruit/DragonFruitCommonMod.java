package com.grim3212.assorted.dragonfruit;

import com.grim3212.assorted.dragonfruit.common.handlers.DragonFruitCreativeItems;
import com.grim3212.assorted.dragonfruit.common.handlers.LootTableHandlers;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.dragonfruit.config.DragonFruitCommonConfig;
import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.AdvancementIcons;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class DragonFruitCommonMod {

    public static final DragonFruitCommonConfig COMMON_CONFIG = new DragonFruitCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "dragon_fruit"), 10)
                .manualOrder(40);

        DragonFruitItems.init();
        DragonFruitCreativeItems.init();

        Services.EVENTS.registerEvent(LootTableModifyEvent.class, (final LootTableModifyEvent event) -> LootTableHandlers.init(event));

        // Recipes and advancements unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
        // The advancement root every part shares: its icon is the first installed of the family's.
        AdvancementIcons.register(Identifier.fromNamespaceAndPath(Constants.FAMILY_ID, "root"), () -> Families.icons(Constants.FAMILY_ID));
    }
}
