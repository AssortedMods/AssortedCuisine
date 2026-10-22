package com.grim3212.assorted.dragonfruit.gametest;

import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Where dragon fruit comes from: a pool added to vanilla's cactus loot table, on top of the cactus itself. */
final class CactusTests {

    private CactusTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("broken_cactus_sometimes_drops_dragon_fruit", CactusTests::brokenCactusSometimesDropsDragonFruit);
    }

    /** At the default one in three, forty breaks always turn up some dragon fruit, and every break still gives the cactus. */
    private static void brokenCactusSometimesDropsDragonFruit(GameTestHelper helper) {
        LootTable table = helper.getLevel().getServer().reloadableRegistries().getLootTable(Blocks.CACTUS.getLootTable().orElseThrow());
        LootParams params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.BLOCK_STATE, Blocks.CACTUS.defaultBlockState())
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(helper.absolutePos(DragonFruitTestSupport.CENTRE)))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .create(LootContextParamSets.BLOCK);

        // One source for every break, as small seeds each give a fresh source nearly the same first roll.
        RandomSource random = RandomSource.create(0L);
        int fruit = 0;
        for (int i = 0; i < 40; i++) {
            List<ItemStack> drops = table.getRandomItems(params, random);
            helper.assertTrue(drops.stream().anyMatch(stack -> stack.is(Items.CACTUS)), "a broken cactus did not drop itself: " + drops);
            fruit += drops.stream().filter(stack -> stack.is(DragonFruitItems.DRAGON_FRUIT.get())).mapToInt(ItemStack::getCount).sum();
        }
        helper.assertTrue(fruit > 0, "forty broken cactus dropped no dragon fruit");
        helper.succeed();
    }
}
