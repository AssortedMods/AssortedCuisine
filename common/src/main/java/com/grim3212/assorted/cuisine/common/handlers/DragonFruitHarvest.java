package com.grim3212.assorted.cuisine.common.handlers;

import com.grim3212.assorted.cuisine.CuisineCommonMod;
import com.grim3212.assorted.cuisine.api.CuisineTags;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.lib.events.UseBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/**
 * Cutting a cactus with a knife takes the fruit off it and leaves the cactus standing, so dragon
 * fruit can be farmed on purpose rather than only turning up when a cactus is broken.
 */
public class DragonFruitHarvest {

    public static void init(UseBlockEvent event) {
        event.setResult(harvest(event.getPlayer(), event.getLevel(), event.getHand(), event.getHitResult().getBlockPos()));
    }

    private static InteractionResult harvest(Player player, Level level, InteractionHand hand, BlockPos pos) {
        if (player.isSpectator() || !CuisineCommonMod.COMMON_CONFIG.dragonFruitEnabled.get()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (!stack.is(CuisineTags.Items.KNIVES) || !level.getBlockState(pos).is(Blocks.CACTUS)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            CuisineBlocks.giveTo(player, new ItemStack(CuisineItems.DRAGON_FRUIT.get()));
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 0.8F, 1.2F);
        }

        return InteractionResult.SUCCESS;
    }
}
