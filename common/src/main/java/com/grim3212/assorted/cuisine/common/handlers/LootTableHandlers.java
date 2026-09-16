package com.grim3212.assorted.cuisine.common.handlers;

import com.grim3212.assorted.cuisine.CuisineCommonMod;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
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

        double chance = CuisineCommonMod.COMMON_CONFIG.dragonFruitChance.get();
        if (!CuisineCommonMod.COMMON_CONFIG.dragonFruitEnabled.get() || chance <= 0.0D) {
            return;
        }

        event.getContext().addPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(CuisineItems.DRAGON_FRUIT.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                .when(LootItemRandomChanceCondition.randomChance((float) chance)));
    }
}
