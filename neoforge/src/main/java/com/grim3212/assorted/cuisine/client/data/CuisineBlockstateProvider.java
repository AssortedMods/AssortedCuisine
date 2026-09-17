package com.grim3212.assorted.cuisine.client.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.block.CuisineMachineBlock;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Block states and block models. This owns every block and block item; {@link
 * CuisineItemModelProvider} owns the rest, so the two never write the same file.
 */
public class CuisineBlockstateProvider extends ModelProvider {

    private static final Identifier MC_BLOCK = Identifier.withDefaultNamespace("block/block");

    /** The inside face a pie shows once it has been bitten into. */
    private static final TextureSlot INSIDE = TextureSlot.create("inside");

    /** A tray on the floor, eight pixels tall: the cheese block and the chocolate mould. */
    private static final ModelTemplate TRAY_ALL = ExtendedModelTemplateBuilder.builder()
            .parent(MC_BLOCK)
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .requiredTextureSlot(TextureSlot.ALL)
            .element(e -> e.from(1, 0, 1).to(15, 8, 15).allFaces((dir, face) -> {
                switch (dir) {
                    case DOWN -> face.texture(TextureSlot.ALL).uvs(1, 1, 15, 15).cullface(Direction.DOWN);
                    case UP -> face.texture(TextureSlot.ALL).uvs(1, 1, 15, 15);
                    default -> face.texture(TextureSlot.ALL).uvs(1, 8, 15, 16);
                }
            }))
            .build();

    private static final ModelTemplate TRAY_TOP_SIDE = ExtendedModelTemplateBuilder.builder()
            .parent(MC_BLOCK)
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .requiredTextureSlot(TextureSlot.TOP)
            .requiredTextureSlot(TextureSlot.SIDE)
            .element(e -> e.from(1, 0, 1).to(15, 8, 15).allFaces((dir, face) -> {
                switch (dir) {
                    case DOWN -> face.texture(TextureSlot.SIDE).uvs(16, 16, 0, 0);
                    case UP -> face.texture(TextureSlot.TOP).uvs(0, 0, 16, 16);
                    default -> face.texture(TextureSlot.SIDE).uvs(0, 10, 16, 16);
                }
            }))
            .build();

    public CuisineBlockstateProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Cuisine block states";
    }

    /**
     * Only the block items belong here; everything else is {@link CuisineItemModelProvider}'s, so
     * the two providers never write the same file.
     */
    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> holder.value() instanceof BlockItem);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(CuisineBlocks.CHOCOLATE_BLOCK.get());

        tray(blockModels, CuisineBlocks.CHEESE_BLOCK.get());
        cheeseMaker(blockModels);
        butterChurn(blockModels);
        chocolateBarMould(blockModels);

        cake(blockModels, CuisineBlocks.CHOCOLATE_CAKE.get(), "chocolate_cake_bottom", "chocolate_cake_side", "chocolate_cake_top", "chocolate_cake_sidecut");
        pie(blockModels, CuisineBlocks.APPLE_PIE.get(), "apple_pie");
        pie(blockModels, CuisineBlocks.MELON_PIE.get(), "melon_pie");
        pie(blockModels, CuisineBlocks.PUMPKIN_PIE.get(), "pumpkin_pie");
        pie(blockModels, CuisineBlocks.CHOCOLATE_PIE.get(), "chocolate_pie");
        pie(blockModels, CuisineBlocks.PORK_PIE.get(), "pork_pie");
    }

    /** A one-model block whose faces all read the same texture. */
    private void tray(BlockModelGenerators blockModels, Block block) {
        Material texture = blockTexture(name(block));
        Identifier model = TRAY_ALL.create(resource("block/" + name(block)), new TextureMapping()
                .put(TextureSlot.PARTICLE, texture)
                .put(TextureSlot.ALL, texture), blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model)));
        blockModels.registerSimpleItemModel(block, model);
    }

    /**
     * The cheese maker is a full cube whose lid shows how far along the milk is: empty, then three
     * stages of curdling. The 1.12 blockstate mapped all sixteen stages onto those four textures.
     */
    private void cheeseMaker(BlockModelGenerators blockModels) {
        Block block = CuisineBlocks.CHEESE_MAKER.get();
        Material side = new Material(Identifier.withDefaultNamespace("block/furnace_side"));

        Identifier empty = cubeTop(blockModels, "cheese_maker", side, blockTexture("cheese_maker_top"));
        Identifier milk1 = cubeTop(blockModels, "cheese_maker_milk1", side, blockTexture("cheese_maker_topmilk"));
        Identifier milk2 = cubeTop(blockModels, "cheese_maker_milk2", side, blockTexture("cheese_maker_topmilk2"));
        Identifier done = cubeTop(blockModels, "cheese_maker_done", side, blockTexture("cheese_maker_topmilk3"));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(CuisineMachineBlock.STAGE).generate(stage -> BlockModelGenerators.plainVariant(
                        stage == 0 ? empty : stage < 8 ? milk1 : stage < CuisineMachineBlock.DONE ? milk2 : done))));
        blockModels.registerSimpleItemModel(block, empty);
    }

    private void butterChurn(BlockModelGenerators blockModels) {
        Block block = CuisineBlocks.BUTTER_CHURN.get();
        Identifier model = cubeTop(blockModels, "butter_churn", blockTexture("butter_churn_side"), blockTexture("butter_churn_top"));

        // Every stage shares one model: there is no second churn texture to show milk in it.
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(CuisineMachineBlock.STAGE).generate(stage -> BlockModelGenerators.plainVariant(model))));
        blockModels.registerSimpleItemModel(block, model);
    }

    private void chocolateBarMould(BlockModelGenerators blockModels) {
        Block block = CuisineBlocks.CHOCOLATE_BAR_MOULD.get();
        Material side = new Material(Identifier.withDefaultNamespace("block/furnace_side"));

        Identifier empty = trayTop(blockModels, "chocolate_bar_mould", side, blockTexture("chocolate_bar_mould"));
        Identifier setting = trayTop(blockModels, "chocolate_bar_mould_cooking", side, blockTexture("chocolate_bar_mould_cooking"));
        Identifier done = trayTop(blockModels, "chocolate_bar_mould_done", side, blockTexture("chocolate_bar_mould_done"));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(CuisineMachineBlock.STAGE).generate(stage -> BlockModelGenerators.plainVariant(
                        stage == 0 ? empty : stage < CuisineMachineBlock.DONE ? setting : done))));
        blockModels.registerSimpleItemModel(block, empty);
    }

    private void pie(BlockModelGenerators blockModels, Block block, String name) {
        cake(blockModels, block, "pie_bottom", "pie_side", name + "_top", name + "_cut");
    }

    /**
     * A cake or a pie: one model per bite, each one starting further east so the eaten side opens
     * up, with the cut face showing the filling.
     */
    private void cake(BlockModelGenerators blockModels, Block block, String bottom, String side, String top, String inside) {
        String name = name(block);
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.PARTICLE, blockTexture(top))
                .put(TextureSlot.BOTTOM, blockTexture(bottom))
                .put(TextureSlot.SIDE, blockTexture(side))
                .put(TextureSlot.TOP, blockTexture(top))
                .put(INSIDE, blockTexture(inside));

        List<Identifier> models = new ArrayList<>();
        for (int bites = 0; bites <= CakeBlock.MAX_BITES; bites++) {
            models.add(cakeTemplate(bites).create(resource("block/" + name + (bites == 0 ? "" : "_slice" + bites)), textures, blockModels.modelOutput));
        }

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(CakeBlock.BITES).generate(bites -> BlockModelGenerators.plainVariant(models.get(bites)))));
        blockModels.registerSimpleItemModel(block, models.get(0));
    }

    private static ModelTemplate cakeTemplate(int bites) {
        final float west = 1 + bites * 2;

        return ExtendedModelTemplateBuilder.builder()
                .parent(MC_BLOCK)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .requiredTextureSlot(TextureSlot.BOTTOM)
                .requiredTextureSlot(TextureSlot.SIDE)
                .requiredTextureSlot(TextureSlot.TOP)
                .requiredTextureSlot(INSIDE)
                .element(e -> e.from(west, 0, 1).to(15, 8, 15).allFaces((dir, face) -> {
                    switch (dir) {
                        case DOWN -> face.texture(TextureSlot.BOTTOM).uvs(west, 1, 15, 15).cullface(Direction.DOWN);
                        case UP -> face.texture(TextureSlot.TOP).uvs(west, 1, 15, 15);
                        // The bitten side shows the filling, the rest keep the crust.
                        case WEST -> face.texture(bites == 0 ? TextureSlot.SIDE : INSIDE).uvs(1, 8, 15, 16);
                        case EAST -> face.texture(TextureSlot.SIDE).uvs(1, 8, 15, 16);
                        default -> face.texture(TextureSlot.SIDE).uvs(west, 8, 15, 16);
                    }
                }))
                .build();
    }

    private Identifier cubeTop(BlockModelGenerators blockModels, String name, Material side, Material top) {
        return ModelTemplates.CUBE_TOP.create(resource("block/" + name), new TextureMapping()
                .put(TextureSlot.PARTICLE, top)
                .put(TextureSlot.SIDE, side)
                .put(TextureSlot.TOP, top), blockModels.modelOutput);
    }

    private Identifier trayTop(BlockModelGenerators blockModels, String name, Material side, Material top) {
        return TRAY_TOP_SIDE.create(resource("block/" + name), new TextureMapping()
                .put(TextureSlot.PARTICLE, top)
                .put(TextureSlot.SIDE, side)
                .put(TextureSlot.TOP, top), blockModels.modelOutput);
    }

    private static String name(Block block) {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private static Material blockTexture(String name) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/" + name));
    }

    private static Identifier resource(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
