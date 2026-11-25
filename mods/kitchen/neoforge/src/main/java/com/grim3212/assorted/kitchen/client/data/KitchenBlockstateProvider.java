package com.grim3212.assorted.kitchen.client.data;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.block.KitchenMachineBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Block states, block models and the block items' models. {@link KitchenItemModelProvider} owns the
 * other items, so the two never write the same file.
 */
public class KitchenBlockstateProvider extends BlockStateProvider {

    private static final ResourceLocation MC_BLOCK = ResourceLocation.withDefaultNamespace("block/block");
    private static final ResourceLocation FURNACE_SIDE = ResourceLocation.withDefaultNamespace("block/furnace_side");

    public KitchenBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Kitchen block states";
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(KitchenBlocks.CHOCOLATE_BLOCK.get(), this.cubeAll(KitchenBlocks.CHOCOLATE_BLOCK.get()));

        this.tray(KitchenBlocks.CHEESE_BLOCK.get());
        this.cheeseMaker();
        this.butterChurn();
        this.chocolateBarMold();

        this.cake(KitchenBlocks.CHOCOLATE_CAKE.get(), "chocolate_cake_bottom", "chocolate_cake_side", "chocolate_cake_top", "chocolate_cake_sidecut");
        this.pie(KitchenBlocks.APPLE_PIE.get(), "apple_pie");
        this.pie(KitchenBlocks.MELON_PIE.get(), "melon_pie");
        this.pie(KitchenBlocks.PUMPKIN_PIE.get(), "pumpkin_pie");
        this.pie(KitchenBlocks.CHOCOLATE_PIE.get(), "chocolate_pie");
        this.pie(KitchenBlocks.PORK_PIE.get(), "pork_pie");
    }

    /** A tray on the floor, eight pixels tall, whose faces all read the same texture: the cheese block. */
    private void tray(Block block) {
        ResourceLocation texture = blockTexture(name(block));
        ModelFile model = this.models().withExistingParent(name(block), MC_BLOCK)
                .texture("particle", texture)
                .texture("all", texture)
                .element().from(1, 0, 1).to(15, 8, 15).allFaces((dir, face) -> {
                    switch (dir) {
                        case DOWN -> face.texture("#all").uvs(1, 1, 15, 15).cullface(Direction.DOWN);
                        case UP -> face.texture("#all").uvs(1, 1, 15, 15);
                        default -> face.texture("#all").uvs(1, 8, 15, 16);
                    }
                }).end();

        this.simpleBlockWithItem(block, model);
    }

    /**
     * The cheese maker is a full cube whose lid shows how far along the milk is: empty, then three
     * stages of curdling. The 1.12 blockstate mapped all sixteen stages onto those four textures.
     */
    private void cheeseMaker() {
        Block block = KitchenBlocks.CHEESE_MAKER.get();

        ModelFile empty = this.models().cubeTop("cheese_maker", FURNACE_SIDE, blockTexture("cheese_maker_top"));
        ModelFile milk1 = this.models().cubeTop("cheese_maker_milk1", FURNACE_SIDE, blockTexture("cheese_maker_topmilk"));
        ModelFile milk2 = this.models().cubeTop("cheese_maker_milk2", FURNACE_SIDE, blockTexture("cheese_maker_topmilk2"));
        ModelFile done = this.models().cubeTop("cheese_maker_done", FURNACE_SIDE, blockTexture("cheese_maker_topmilk3"));

        this.byStage(block, stage -> stage == 0 ? empty : stage < 8 ? milk1 : stage < KitchenMachineBlock.DONE ? milk2 : done);
        this.simpleBlockItem(block, empty);
    }

    private void butterChurn() {
        Block block = KitchenBlocks.BUTTER_CHURN.get();
        ModelFile model = this.models().cubeTop("butter_churn", blockTexture("butter_churn_side"), blockTexture("butter_churn_top"));

        // Every stage shares one model: there is no second churn texture to show milk in it.
        this.byStage(block, stage -> model);
        this.simpleBlockItem(block, model);
    }

    private void chocolateBarMold() {
        Block block = KitchenBlocks.CHOCOLATE_BAR_MOLD.get();

        ModelFile empty = this.trayTop("chocolate_bar_mold", blockTexture("chocolate_bar_mold"));
        ModelFile setting = this.trayTop("chocolate_bar_mold_cooking", blockTexture("chocolate_bar_mold_cooking"));
        ModelFile done = this.trayTop("chocolate_bar_mold_done", blockTexture("chocolate_bar_mold_done"));

        this.byStage(block, stage -> stage == 0 ? empty : stage < KitchenMachineBlock.DONE ? setting : done);
        this.simpleBlockItem(block, empty);
    }

    private void byStage(Block block, IntFunction<ModelFile> model) {
        this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model.apply(state.getValue(KitchenMachineBlock.STAGE))).build());
    }

    private void pie(Block block, String name) {
        this.cake(block, "pie_bottom", "pie_side", name + "_top", name + "_cut");
    }

    /**
     * A cake or a pie: one model per bite, each one starting further east so the eaten side opens
     * up, with the cut face showing the filling.
     */
    private void cake(Block block, String bottom, String side, String top, String inside) {
        String name = name(block);

        List<ModelFile> models = new ArrayList<>();
        for (int bites = 0; bites <= CakeBlock.MAX_BITES; bites++) {
            final int bitten = bites;
            final float west = 1 + bites * 2;

            BlockModelBuilder model = this.models().withExistingParent(name + (bites == 0 ? "" : "_slice" + bites), MC_BLOCK)
                    .texture("particle", blockTexture(top))
                    .texture("bottom", blockTexture(bottom))
                    .texture("side", blockTexture(side))
                    .texture("top", blockTexture(top))
                    .texture("inside", blockTexture(inside))
                    .element().from(west, 0, 1).to(15, 8, 15).allFaces((dir, face) -> {
                        switch (dir) {
                            case DOWN -> face.texture("#bottom").uvs(west, 1, 15, 15).cullface(Direction.DOWN);
                            case UP -> face.texture("#top").uvs(west, 1, 15, 15);
                            // The bitten side shows the filling, the rest keep the crust.
                            case WEST -> face.texture(bitten == 0 ? "#side" : "#inside").uvs(1, 8, 15, 16);
                            case EAST -> face.texture("#side").uvs(1, 8, 15, 16);
                            default -> face.texture("#side").uvs(west, 8, 15, 16);
                        }
                    }).end();
            models.add(model);
        }

        this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(models.get(state.getValue(CakeBlock.BITES))).build());
        this.simpleBlockItem(block, models.get(0));
    }

    /** A tray with a top of its own, the chocolate mold. */
    private ModelFile trayTop(String name, ResourceLocation top) {
        return this.models().withExistingParent(name, MC_BLOCK)
                .texture("particle", top)
                .texture("side", FURNACE_SIDE)
                .texture("top", top)
                .element().from(1, 0, 1).to(15, 8, 15).allFaces((dir, face) -> {
                    switch (dir) {
                        case DOWN -> face.texture("#side").uvs(16, 16, 0, 0);
                        case UP -> face.texture("#top").uvs(0, 0, 16, 16);
                        default -> face.texture("#side").uvs(0, 10, 16, 16);
                    }
                }).end();
    }

    private static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private static ResourceLocation blockTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/" + name);
    }
}
