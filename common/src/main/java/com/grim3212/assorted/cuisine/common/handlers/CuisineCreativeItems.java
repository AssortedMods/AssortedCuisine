package com.grim3212.assorted.cuisine.common.handlers;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.CuisineCommonMod;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.cuisine.config.CuisineCommonConfig;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CuisineCreativeItems {

    public static final RegistryProvider<CreativeModeTab> CREATIVE_TABS = RegistryProvider.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tab"));

    // CreativeModeTab.Output is protected in vanilla, so the tab is registered empty and filled
    // through modifyCreativeTab. The builder is deprecated only by NeoForge's patches.
    @SuppressWarnings("deprecation")
    public static final IRegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("tab", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup." + Constants.MOD_ID))
            .icon(() -> new ItemStack(CuisineItems.CHEESE.get()))
            .build());

    private static List<ItemStack> getCreativeItems() {
        CuisineCommonConfig config = CuisineCommonMod.COMMON_CONFIG;
        CreativeTabItems items = new CreativeTabItems();

        if (config.dairyEnabled.get()) {
            items.add(CuisineBlocks.BUTTER_CHURN.get());
            items.add(CuisineBlocks.CHEESE_MAKER.get());
            items.add(CuisineBlocks.CHEESE_BLOCK.get());
            items.add(CuisineItems.BUTTER.get());
            items.add(CuisineItems.CHEESE.get());
        }

        // Dairy and pies both cut with it.
        if (config.dairyEnabled.get() || config.piesEnabled.get()) {
            items.add(CuisineItems.KNIFE.get());
        }

        if (config.dairyEnabled.get()) {
            items.add(CuisineItems.MIXER.get());
            items.add(CuisineItems.BREAD_SLICE.get());
            items.add(CuisineItems.CHEESE_BURGER.get());
            items.add(CuisineItems.HOT_CHEESE.get());
            items.add(CuisineItems.EGGS_UNMIXED.get());
            items.add(CuisineItems.EGGS_MIXED.get());
            items.add(CuisineItems.EGGS_COOKED.get());
        }

        if (config.chocolateEnabled.get()) {
            items.add(CuisineItems.COCOA_DUST.get());
            items.add(CuisineItems.CHOCOLATE_BOWL.get());
            items.add(CuisineItems.HOT_CHOCOLATE.get());
            items.add(CuisineBlocks.CHOCOLATE_BAR_MOULD.get());
            items.add(CuisineItems.CHOCOLATE_BAR.get());
            items.add(CuisineItems.WRAPPER.get());
            items.add(CuisineItems.CHOCOLATE_BAR_WRAPPED.get());
            items.add(CuisineItems.CHOCOLATE_BALL.get());
            items.add(CuisineBlocks.CHOCOLATE_BLOCK.get());
            items.add(CuisineBlocks.CHOCOLATE_CAKE.get());
        }

        if (config.piesEnabled.get()) {
            // The chocolate pie is filled with chocolate balls, so it needs both.
            boolean chocolatePie = config.chocolateEnabled.get();

            items.add(CuisineItems.PAN.get());
            items.add(CuisineItems.DOUGH.get());
            items.add(CuisineItems.PUMPKIN_SLICE.get());
            items.add(CuisineItems.RAW_EMPTY_PIE.get());
            items.add(CuisineItems.RAW_APPLE_PIE.get());
            items.add(CuisineItems.RAW_MELON_PIE.get());
            items.add(CuisineItems.RAW_PUMPKIN_PIE.get());
            if (chocolatePie) {
                items.add(CuisineItems.RAW_CHOCOLATE_PIE.get());
            }
            items.add(CuisineItems.RAW_PORK_PIE.get());
            items.add(CuisineBlocks.APPLE_PIE.get());
            items.add(CuisineBlocks.MELON_PIE.get());
            items.add(CuisineBlocks.PUMPKIN_PIE.get());
            if (chocolatePie) {
                items.add(CuisineBlocks.CHOCOLATE_PIE.get());
            }
            items.add(CuisineBlocks.PORK_PIE.get());
        }

        if (config.healthEnabled.get()) {
            items.add(CuisineItems.SWEETS.get());
            items.add(CuisineItems.POWERED_SUGAR.get());
            items.add(CuisineItems.POWERED_SWEETS.get());
            items.add(CuisineItems.BANDAGE.get());
            items.add(CuisineItems.HEALTHPACK.get());
            items.add(CuisineItems.HEALTHPACK_SUPER.get());
        }

        if (config.dragonFruitEnabled.get()) {
            items.add(CuisineItems.DRAGON_FRUIT.get());
        }

        if (config.sodaEnabled.get()) {
            items.add(CuisineItems.SODA_BOTTLE.get());
            items.add(CuisineItems.SODA_CO2.get());
            items.add(CuisineItems.SODA_CARBONATED_WATER.get());
            items.add(CuisineItems.SODA_APPLE.get());
            items.add(CuisineItems.SODA_ORANGE.get());
            items.add(CuisineItems.SODA_CREAM_ORANGE.get());
            items.add(CuisineItems.SODA_SPIKED_ORANGE.get());
            items.add(CuisineItems.SODA_ROOT_BEER.get());
            items.add(CuisineItems.SODA_COCOA.get());
            items.add(CuisineItems.SODA_GOLDEN_APPLE.get());
            items.add(CuisineItems.SODA_DIAMOND.get());
            items.add(CuisineItems.SODA_MUSHROOM.get());
            items.add(CuisineItems.SODA_SLURM.get());
        }

        return items.getItems();
    }

    public static void init() {
        Services.PLATFORM.modifyCreativeTab(CREATIVE_TAB_KEY, CuisineCreativeItems::getCreativeItems);
    }
}
