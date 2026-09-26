package com.grim3212.assorted.kitchen.common.handlers;

import com.grim3212.assorted.kitchen.KitchenCommonMod;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Cactus drops dragon fruit. 1.12 did this by intercepting the harvest event; a loot pool is the
 * modern equivalent and a datapack can turn it off.
 */
public class LootTableHandlers {

    private static final Identifier CACTUS = Identifier.withDefaultNamespace("blocks/cactus");

    public static void init(LootTableModifyEvent event) {
        if (!event.getId().equals(CACTUS)) {
            return;
        }

        double chance = KitchenCommonMod.COMMON_CONFIG.dragonFruitChance.get();
        if (!KitchenCommonMod.COMMON_CONFIG.dragonFruitEnabled.get() || chance <= 0.0D) {
            return;
        }

        event.getContext().addPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(KitchenItems.DRAGON_FRUIT.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                .when(LootItemRandomChanceCondition.randomChance((float) chance)));
    }
}
