package com.grim3212.assorted.cuisine.client.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.crafting.CuisineConditions.Parts;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This mod's section of the instruction manual. Each chapter hangs off the subpart it documents, so
 * a disabled part takes its pages with it.
 */
public class CuisineManualProvider extends LibManualProvider {

    public CuisineManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addChapters() {
        this.section(40, CuisineItems.CHEESE_BURGER.get());

        this.addCocoa();
        this.addChocolate();
        this.addDairy();
        this.addFood();
        this.addDragonFruit();
        this.addPies();
        this.addSoda();
        this.addSugar();
        this.addHealth();
    }

    private void addCocoa() {
        ChapterBuilder cocoa = this.chapter("cocoa").whenPartEnabled(Parts.CHOCOLATE);

        // The pod and the sapling are blocks with no item of their own; the fruit plants them.
        cocoa.image("tree", picture("cocoa_tree"), 90, 104)
                .opens(CuisineItems.COCOA_FRUIT.get(), CuisineBlocks.COCOA_POD.get(), CuisineBlocks.COCOA_SAPLING.get());
        cocoa.recipes("dust", CuisineItems.COCOA_DUST.get()).opens(CuisineItems.COCOA_DUST.get());
    }

    private void addChocolate() {
        ChapterBuilder chocolate = this.chapter("chocolate").whenPartEnabled(Parts.CHOCOLATE);

        chocolate.recipes("bowl", CuisineItems.CHOCOLATE_BOWL.get()).opens(CuisineItems.CHOCOLATE_BOWL.get());
        chocolate.recipesById("hot", recipeId("hot_chocolate_smelting")).opens(CuisineItems.HOT_CHOCOLATE.get());
        chocolate.recipes("ball", CuisineItems.CHOCOLATE_BALL.get()).opens(CuisineItems.CHOCOLATE_BALL.get());
        chocolate.recipes("mould", CuisineBlocks.CHOCOLATE_BAR_MOULD.get()).opens(CuisineBlocks.CHOCOLATE_BAR_MOULD.get());
        chocolate.recipes("bar", CuisineItems.CHOCOLATE_BAR.get()).opens(CuisineItems.CHOCOLATE_BAR.get());
        chocolate.recipes("wrapped", CuisineItems.WRAPPER.get(), CuisineItems.CHOCOLATE_BAR_WRAPPED.get()).every(60)
                .opens(CuisineItems.WRAPPER.get(), CuisineItems.CHOCOLATE_BAR_WRAPPED.get());
        chocolate.recipes("storage", CuisineBlocks.CHOCOLATE_BLOCK.get()).opens(CuisineBlocks.CHOCOLATE_BLOCK.get());
        chocolate.recipes("cake", CuisineBlocks.CHOCOLATE_CAKE.get()).opens(CuisineBlocks.CHOCOLATE_CAKE.get());
    }

    private void addDairy() {
        ChapterBuilder dairy = this.chapter("dairy").whenPartEnabled(Parts.DAIRY);

        dairy.recipes("butter_churn", CuisineBlocks.BUTTER_CHURN.get())
                .opens(CuisineBlocks.BUTTER_CHURN.get(), CuisineItems.BUTTER.get());
        dairy.recipes("cheese_maker", CuisineBlocks.CHEESE_MAKER.get()).opens(CuisineBlocks.CHEESE_MAKER.get());
        dairy.recipes("cheese", CuisineItems.CHEESE.get(), CuisineBlocks.CHEESE_BLOCK.get()).every(60)
                .opens(CuisineItems.CHEESE.get(), CuisineBlocks.CHEESE_BLOCK.get());
        dairy.recipes("hot_cheese", CuisineItems.HOT_CHEESE.get()).opens(CuisineItems.HOT_CHEESE.get());
    }

    private void addFood() {
        ChapterBuilder food = this.chapter("food");

        food.recipesById("eggs", recipeId(CuisineItems.EGGS_UNMIXED.get()), recipeId(CuisineItems.EGGS_MIXED.get()), recipeId("eggs_cooked_smelting")).every(60)
                .whenPartEnabled(Parts.DAIRY)
                .opens(CuisineItems.EGGS_UNMIXED.get(), CuisineItems.EGGS_MIXED.get(), CuisineItems.EGGS_COOKED.get());
        food.recipes("sandwiches", CuisineItems.BREAD_SLICE.get(), CuisineItems.CHEESE_BURGER.get()).every(60)
                .whenPartEnabled(Parts.DAIRY)
                .opens(CuisineItems.BREAD_SLICE.get(), CuisineItems.CHEESE_BURGER.get());
        // Dairy slices bread with it and pies slice pumpkins, so either part keeps it.
        food.recipes("knife", CuisineItems.KNIFE.get())
                .when(anyOf(partEnabled(Parts.DAIRY), partEnabled(Parts.PIES)))
                .opens(CuisineItems.KNIFE.get());
        food.recipes("mixer", CuisineItems.MIXER.get()).whenPartEnabled(Parts.DAIRY).opens(CuisineItems.MIXER.get());
    }

    private void addDragonFruit() {
        ChapterBuilder dragonFruit = this.chapter("dragon_fruit").whenPartEnabled(Parts.DRAGON_FRUIT);

        dragonFruit.image("cactus", picture("dragon_fruit"), 69, 104).opens(CuisineItems.DRAGON_FRUIT.get());
    }

    private void addPies() {
        ChapterBuilder pies = this.chapter("pies").whenPartEnabled(Parts.PIES);

        pies.recipes("ingredients", CuisineItems.PAN.get(), CuisineItems.DOUGH.get(), CuisineItems.PUMPKIN_SLICE.get()).every(60)
                .opens(CuisineItems.PAN.get(), CuisineItems.DOUGH.get(), CuisineItems.PUMPKIN_SLICE.get());
        pies.recipes("raw", CuisineItems.RAW_EMPTY_PIE.get(), CuisineItems.RAW_APPLE_PIE.get(), CuisineItems.RAW_PUMPKIN_PIE.get(), CuisineItems.RAW_MELON_PIE.get(), CuisineItems.RAW_PORK_PIE.get()).every(60)
                .opens(CuisineItems.RAW_EMPTY_PIE.get(), CuisineItems.RAW_APPLE_PIE.get(),
                        CuisineItems.RAW_PUMPKIN_PIE.get(), CuisineItems.RAW_MELON_PIE.get(),
                        CuisineItems.RAW_PORK_PIE.get());
        pies.recipesById("baking", recipeId("apple_pie_smelting"), recipeId("pumpkin_pie_smelting"), recipeId("melon_pie_smelting"), recipeId("pork_pie_smelting")).every(60)
                .opens(CuisineBlocks.APPLE_PIE.get(), CuisineBlocks.PUMPKIN_PIE.get(), CuisineBlocks.MELON_PIE.get(),
                        CuisineBlocks.PORK_PIE.get());
        // Its own page: the recipes only exist while chocolate is on too, and a page naming a
        // missing recipe draws an error.
        pies.recipesById("chocolate", recipeId(CuisineItems.RAW_CHOCOLATE_PIE.get()), recipeId("chocolate_pie_smelting")).every(60)
                .whenPartEnabled(Parts.CHOCOLATE)
                .opens(CuisineItems.RAW_CHOCOLATE_PIE.get(), CuisineBlocks.CHOCOLATE_PIE.get());
    }

    private void addSoda() {
        ChapterBuilder soda = this.chapter("soda").whenPartEnabled(Parts.SODA);

        soda.recipes("carbonated", CuisineItems.SODA_BOTTLE.get(), CuisineItems.SODA_CO2.get(), CuisineItems.SODA_CARBONATED_WATER.get()).every(60)
                .opens(CuisineItems.SODA_BOTTLE.get(), CuisineItems.SODA_CO2.get(),
                        CuisineItems.SODA_CARBONATED_WATER.get());
        soda.recipes("types", CuisineItems.SODA_SLURM.get(), CuisineItems.SODA_APPLE.get(), CuisineItems.SODA_GOLDEN_APPLE.get(), CuisineItems.SODA_COCOA.get(), CuisineItems.SODA_ROOT_BEER.get(), CuisineItems.SODA_DIAMOND.get(), CuisineItems.SODA_MUSHROOM.get(), CuisineItems.SODA_ORANGE.get(), CuisineItems.SODA_CREAM_ORANGE.get(), CuisineItems.SODA_SPIKED_ORANGE.get())
                .every(60)
                .opens(CuisineItems.SODA_SLURM.get(), CuisineItems.SODA_APPLE.get(),
                        CuisineItems.SODA_GOLDEN_APPLE.get(), CuisineItems.SODA_COCOA.get(),
                        CuisineItems.SODA_ROOT_BEER.get(), CuisineItems.SODA_DIAMOND.get(),
                        CuisineItems.SODA_MUSHROOM.get(), CuisineItems.SODA_ORANGE.get(),
                        CuisineItems.SODA_CREAM_ORANGE.get(), CuisineItems.SODA_SPIKED_ORANGE.get());
    }

    private void addSugar() {
        ChapterBuilder sugar = this.chapter("sugar").whenPartEnabled(Parts.HEALTH);

        sugar.recipes("sweets", CuisineItems.POWERED_SUGAR.get(), CuisineItems.SWEETS.get(), CuisineItems.POWERED_SWEETS.get()).every(60)
                .opens(CuisineItems.POWERED_SUGAR.get(), CuisineItems.SWEETS.get(), CuisineItems.POWERED_SWEETS.get());
    }

    private void addHealth() {
        ChapterBuilder health = this.chapter("health").whenPartEnabled(Parts.HEALTH);

        health.recipes("packs", CuisineItems.BANDAGE.get(), CuisineItems.HEALTHPACK.get(), CuisineItems.HEALTHPACK_SUPER.get()).every(60)
                .opens(CuisineItems.BANDAGE.get(), CuisineItems.HEALTHPACK.get(), CuisineItems.HEALTHPACK_SUPER.get());
    }

    /** The Grim Pack screenshots under {@code textures/gui/manual}, cropped and sized to leave room for the text. */
    private static Identifier picture(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/manual/" + name + ".png");
    }
}
