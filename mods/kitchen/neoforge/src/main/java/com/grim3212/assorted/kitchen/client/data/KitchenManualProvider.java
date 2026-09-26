package com.grim3212.assorted.kitchen.client.data;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.Family;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapters of the Assorted Cuisine section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class KitchenManualProvider extends LibManualProvider {

    public KitchenManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        this.addChocolate();
        this.addDairy();
        this.addFood();
        this.addPies();
    }

    private void addChocolate() {
        ChapterBuilder chocolate = this.chapter("chocolate", 0);

        chocolate.recipes("mortar_and_pestle", KitchenItems.MORTAR_AND_PESTLE.get()).opens(KitchenItems.MORTAR_AND_PESTLE.get());
        chocolate.recipes("dust", KitchenItems.COCOA_DUST.get()).opens(KitchenItems.COCOA_DUST.get());
        chocolate.recipes("bowl", KitchenItems.CHOCOLATE_BOWL.get()).opens(KitchenItems.CHOCOLATE_BOWL.get());
        chocolate.recipesById("hot", recipeId("hot_chocolate_smelting")).opens(KitchenItems.HOT_CHOCOLATE.get());
        chocolate.recipes("ball", KitchenItems.CHOCOLATE_BALL.get()).opens(KitchenItems.CHOCOLATE_BALL.get());
        chocolate.recipes("mould", KitchenBlocks.CHOCOLATE_BAR_MOULD.get()).opens(KitchenBlocks.CHOCOLATE_BAR_MOULD.get());
        // What the mould does once it is placed, which is not a crafting recipe.
        chocolate.recipesById("moulding", recipeId("chocolate_moulding"));
        chocolate.recipes("bar", KitchenItems.CHOCOLATE_BAR.get()).opens(KitchenItems.CHOCOLATE_BAR.get());
        chocolate.recipes("wrapped", KitchenItems.WRAPPER.get(), KitchenItems.CHOCOLATE_BAR_WRAPPED.get()).every(60)
                .opens(KitchenItems.WRAPPER.get(), KitchenItems.CHOCOLATE_BAR_WRAPPED.get());
        chocolate.recipes("storage", KitchenBlocks.CHOCOLATE_BLOCK.get()).opens(KitchenBlocks.CHOCOLATE_BLOCK.get());
        chocolate.recipes("cake", KitchenBlocks.CHOCOLATE_CAKE.get()).opens(KitchenBlocks.CHOCOLATE_CAKE.get());
    }

    private void addDairy() {
        ChapterBuilder dairy = this.chapter("dairy", 1);

        dairy.recipes("butter_churn", KitchenBlocks.BUTTER_CHURN.get())
                .opens(KitchenBlocks.BUTTER_CHURN.get(), KitchenItems.BUTTER.get());
        dairy.recipes("cheese_maker", KitchenBlocks.CHEESE_MAKER.get()).opens(KitchenBlocks.CHEESE_MAKER.get());
        // What the two machines do once placed; neither is a crafting recipe.
        dairy.recipesById("making", recipeId("cheese_making"), recipeId("churning")).every(60);
        dairy.recipes("cheese", KitchenItems.CHEESE.get(), KitchenBlocks.CHEESE_BLOCK.get()).every(60)
                .opens(KitchenItems.CHEESE.get(), KitchenBlocks.CHEESE_BLOCK.get());
        dairy.recipes("hot_cheese", KitchenItems.HOT_CHEESE.get()).opens(KitchenItems.HOT_CHEESE.get());
    }

    private void addFood() {
        ChapterBuilder food = this.chapter("food", 2);

        food.recipesById("eggs", recipeId(KitchenItems.EGGS_UNMIXED.get()), recipeId(KitchenItems.EGGS_MIXED.get()), recipeId("eggs_cooked_smelting")).every(60)
                .opens(KitchenItems.EGGS_UNMIXED.get(), KitchenItems.EGGS_MIXED.get(), KitchenItems.EGGS_COOKED.get());
        food.recipes("sandwiches", KitchenItems.BREAD_SLICE.get(), KitchenItems.CHEESE_BURGER.get()).every(60)
                .opens(KitchenItems.BREAD_SLICE.get(), KitchenItems.CHEESE_BURGER.get());
        food.recipes("knife", KitchenItems.KNIFE.get()).opens(KitchenItems.KNIFE.get());
        food.recipes("whisk", KitchenItems.WHISK.get()).opens(KitchenItems.WHISK.get());
    }

    private void addPies() {
        ChapterBuilder pies = this.chapter("pies", 4);

        pies.recipes("ingredients", KitchenItems.PAN.get(), KitchenItems.DOUGH.get(), KitchenItems.PUMPKIN_SLICE.get()).every(60)
                .opens(KitchenItems.PAN.get(), KitchenItems.DOUGH.get(), KitchenItems.PUMPKIN_SLICE.get());
        pies.recipes("raw", KitchenItems.RAW_EMPTY_PIE.get(), KitchenItems.RAW_APPLE_PIE.get(), KitchenItems.RAW_PUMPKIN_PIE.get(), KitchenItems.RAW_MELON_PIE.get(), KitchenItems.RAW_PORK_PIE.get()).every(60)
                .opens(KitchenItems.RAW_EMPTY_PIE.get(), KitchenItems.RAW_APPLE_PIE.get(),
                        KitchenItems.RAW_PUMPKIN_PIE.get(), KitchenItems.RAW_MELON_PIE.get(),
                        KitchenItems.RAW_PORK_PIE.get());
        pies.recipesById("baking", recipeId("apple_pie_smelting"), recipeId("pumpkin_pie_smelting"), recipeId("melon_pie_smelting"), recipeId("pork_pie_smelting")).every(60)
                .opens(KitchenBlocks.APPLE_PIE.get(), KitchenBlocks.PUMPKIN_PIE.get(), KitchenBlocks.MELON_PIE.get(),
                        KitchenBlocks.PORK_PIE.get());
        pies.recipesById("chocolate", recipeId(KitchenItems.RAW_CHOCOLATE_PIE.get()), recipeId("chocolate_pie_smelting")).every(60)
                .opens(KitchenItems.RAW_CHOCOLATE_PIE.get(), KitchenBlocks.CHOCOLATE_PIE.get());
    }
}
