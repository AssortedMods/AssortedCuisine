package com.grim3212.assorted.cuisine.common.block;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;
import java.util.function.Supplier;

public class CuisineBlocks {

    public static final RegistryProvider<Block> BLOCKS = RegistryProvider.create(Registries.BLOCK, Constants.MOD_ID);
    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID);

    public static final IRegistryObject<CheeseBlock> CHEESE_BLOCK = register("cheese_block", props -> new CheeseBlock(props.mapColor(MapColor.SAND).sound(SoundType.WOOL).strength(0.5F).noOcclusion()));
    public static final IRegistryObject<CheeseMakerBlock> CHEESE_MAKER = register("cheese_maker", props -> new CheeseMakerBlock(props.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F).noOcclusion()));
    public static final IRegistryObject<ButterChurnBlock> BUTTER_CHURN = register("butter_churn", props -> new ButterChurnBlock(props.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F)));

    public static final IRegistryObject<ChocolateBarMouldBlock> CHOCOLATE_BAR_MOULD = register("chocolate_bar_mould", props -> new ChocolateBarMouldBlock(props.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.0F).noOcclusion()));
    public static final IRegistryObject<Block> CHOCOLATE_BLOCK = register("chocolate_block", props -> new Block(props.mapColor(MapColor.DIRT).sound(SoundType.WOOL).strength(1.0F)));
    public static final IRegistryObject<CuisineCakeBlock> CHOCOLATE_CAKE = register("chocolate_cake", props -> new CuisineCakeBlock(props.mapColor(MapColor.DIRT).sound(SoundType.WOOL).strength(0.5F).pushReaction(PushReaction.DESTROY)));

    public static final IRegistryObject<CuisineCakeBlock> APPLE_PIE = registerPie("apple_pie");
    public static final IRegistryObject<CuisineCakeBlock> MELON_PIE = registerPie("melon_pie");
    public static final IRegistryObject<CuisineCakeBlock> PUMPKIN_PIE = registerPie("pumpkin_pie");
    public static final IRegistryObject<CuisineCakeBlock> CHOCOLATE_PIE = registerPie("chocolate_pie");
    public static final IRegistryObject<CuisineCakeBlock> PORK_PIE = registerPie("pork_pie");

    private static IRegistryObject<CuisineCakeBlock> registerPie(String name) {
        return register(name, props -> new CuisineCakeBlock(props.mapColor(MapColor.SAND).sound(SoundType.WOOL).strength(0.5F).pushReaction(PushReaction.DESTROY)));
    }

    /**
     * Hands something a right click produced straight to the player. Everything this mod gives back
     * for a click goes through here rather than onto the floor; the inventory drops it at their feet
     * if there is no room.
     */
    public static void giveTo(Player player, ItemStack stack) {
        if (!stack.isEmpty()) {
            player.getInventory().placeItemBackInInventory(stack);
        }
    }

    private static <T extends Block> IRegistryObject<T> register(String name, Function<BlockBehaviour.Properties, ? extends T> factory) {
        // Since 1.21.2 every block has to know its own id before it is constructed, so the
        // properties are built here where the registration name is known.
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        IRegistryObject<T> ret = BLOCKS.register(name, () -> factory.apply(BlockBehaviour.Properties.of().setId(key)));
        ITEMS.register(name, item(name, ret));
        return ret;
    }

    private static Supplier<BlockItem> item(final String name, final IRegistryObject<? extends Block> block) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return () -> new BlockItem(block.get(), new Item.Properties().useBlockDescriptionPrefix().setId(key));
    }

    public static void init() {
    }
}
