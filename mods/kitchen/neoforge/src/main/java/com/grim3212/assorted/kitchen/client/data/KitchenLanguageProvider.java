package com.grim3212.assorted.kitchen.client.data;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block or item whose name is its id in title case needs no line here (see
 * {@link LibLanguageProvider}); the manual's keys are the Assorted Cuisine section's, which every part shares.
 */
public class KitchenLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public KitchenLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedcuisine", "Assorted Cuisine");

        // Names that are not just the id in title case.
        this.add("item.assortedkitchen.eggs_unmixed", "Cracked Eggs");
        this.add("item.assortedkitchen.eggs_mixed", "Mixed Eggs");
        this.add("item.assortedkitchen.eggs_cooked", "Scrambled Eggs");
        this.add("item.assortedkitchen.hot_cheese", "Cheese Sandwich");
        this.add("item.assortedkitchen.pan", "Pie Pan");
        this.add("item.assortedkitchen.mortar_and_pestle", "Mortar and Pestle");
        this.add("item.assortedkitchen.raw_empty_pie", "Unbaked Pie Shell");
        this.add("item.assortedkitchen.raw_apple_pie", "Unbaked Apple Pie");
        this.add("item.assortedkitchen.raw_melon_pie", "Unbaked Melon Pie");
        this.add("item.assortedkitchen.raw_pumpkin_pie", "Unbaked Pumpkin Pie");
        this.add("item.assortedkitchen.raw_chocolate_pie", "Unbaked Chocolate Pie");
        this.add("item.assortedkitchen.raw_pork_pie", "Unbaked Pork Pie");
        this.add("item.assortedkitchen.chocolate_bowl", "Bowl of Chocolate");
        this.add("item.assortedkitchen.hot_chocolate", "Hot Chocolate");

        // The machine slot in the manual's recipe layouts and on JEI's page, both of which format
        // it with the machine's own name.
        this.add("tooltip.assortedkitchen.made_in", "Made in %s");

        this.add("tag.item.assortedkitchen.pies", "Pies");
        this.add("tag.item.c.tools.knife", "Knives");
        this.add("tag.item.c.tools.whisk", "Whisks");
        this.add("tag.item.c.tools.mortar_and_pestle", "Mortars and Pestles");
        this.add("tag.item.c.foods.cheese", "Cheeses");
        this.add("tag.item.c.foods.butter", "Butters");
        this.add("tag.item.c.foods.dough", "Doughs");
        this.add("tag.item.c.foods.cooked_egg", "Cooked Eggs");
        this.add("tag.item.c.foods.bread", "Breads");

        this.addManual();
    }

    /** This part's chapters of the Assorted Cuisine section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedcuisine.title", "Assorted Cuisine");
        this.add("manual.assortedcuisine.description",
                "Cocoa, chocolate, dairy and everything else this mod gives you to cook, bottle and eat.");

        this.addChocolateChapter();
        this.addDairyChapter();
        this.addFoodChapter();
        this.addPiesChapter();
        this.addAdvancements();
    }

    private void addChocolateChapter() {
        this.add("manual.assortedcuisine.chapter.chocolate", "Chocolate");

        this.add("manual.assortedcuisine.chapter.chocolate.mortar_and_pestle.title", "Mortar and Pestle");
        this.add("manual.assortedcuisine.chapter.chocolate.mortar_and_pestle",
                "The mortar and pestle grinds cocoa beans. It stays in the crafting grid and wears out a little "
                        + "with every use.");

        this.add("manual.assortedcuisine.chapter.chocolate.dust.title", "Cocoa Dust");
        this.add("manual.assortedcuisine.chapter.chocolate.dust",
                "Ground down in a mortar and pestle, cocoa beans give cocoa dust.");

        this.add("manual.assortedcuisine.chapter.chocolate.bowl.title", "Chocolate Bowl");
        this.add("manual.assortedcuisine.chapter.chocolate.bowl",
                "Cocoa dust and milk in a bowl is cold chocolate, which is not much use on its own. Any milk "
                        + "bucket will do, including the ones Assorted Tools adds.");

        this.add("manual.assortedcuisine.chapter.chocolate.hot.title", "Hot Chocolate");
        this.add("manual.assortedcuisine.chapter.chocolate.hot",
                "Heat the bowl in a furnace and it becomes hot chocolate. Everything below wants it hot.");

        this.add("manual.assortedcuisine.chapter.chocolate.ball.title", "Chocolate Balls");
        this.add("manual.assortedcuisine.chapter.chocolate.ball",
                "Hot chocolate rolled into balls is the quickest thing here to eat, and what the cake is "
                        + "built from.");

        this.add("manual.assortedcuisine.chapter.chocolate.mold.title", "Chocolate Bar Mold");
        this.add("manual.assortedcuisine.chapter.chocolate.mold",
                "Place the mold and right click it with hot chocolate to pour." + BREAK
                        + "It steams while it sets, and every block of ice or snow packed against its sides "
                        + "cools it faster." + BREAK
                        + "Once it has set, right click the mold for the bars.");

        this.add("manual.assortedcuisine.chapter.chocolate.molding.title", "Setting Bars");
        this.add("manual.assortedcuisine.chapter.chocolate.molding",
                "One bowl of hot chocolate sets into two bars.");

        this.add("manual.assortedcuisine.chapter.chocolate.bar.title", "Chocolate Bars");
        this.add("manual.assortedcuisine.chapter.chocolate.bar",
                "A plain bar is food on its own, and the base of everything wrapped.");

        this.add("manual.assortedcuisine.chapter.chocolate.wrapped.title", "Wrapped Bars");
        this.add("manual.assortedcuisine.chapter.chocolate.wrapped",
                "Make a wrapper, wrap a bar in it, and the result feeds you better than the bare bar did.");

        this.add("manual.assortedcuisine.chapter.chocolate.storage.title", "Storage");
        this.add("manual.assortedcuisine.chapter.chocolate.storage",
                "Nine bars press into a block, and a block gives the nine back.");

        this.add("manual.assortedcuisine.chapter.chocolate.cake.title", "Chocolate Cake");
        this.add("manual.assortedcuisine.chapter.chocolate.cake",
                "Chocolate balls and milk make a cake. It is placed and eaten a slice at a time, the way any "
                        + "cake is.");
    }

    private void addDairyChapter() {
        this.add("manual.assortedcuisine.chapter.dairy", "Dairy");

        this.add("manual.assortedcuisine.chapter.dairy.butter_churn.title", "Butter Churn");
        this.add("manual.assortedcuisine.chapter.dairy.butter_churn",
                "Right click the churn with any milk bucket to fill it. It separates on its own, but every "
                        + "right click while it works gives the handle a turn and hurries it along." + BREAK
                        + "Right click once more when it is done for two butter.");

        this.add("manual.assortedcuisine.chapter.dairy.cheese_maker.title", "Cheese Maker");
        this.add("manual.assortedcuisine.chapter.dairy.cheese_maker",
                "The cheese maker takes a milk bucket the same way, but it needs longer and cannot be hurried. "
                        + "Watch it turn from pale to a yellow orange." + BREAK
                        + "When it has gone the whole way, right click it to get your cheese.");

        this.add("manual.assortedcuisine.chapter.dairy.making.title", "What Goes In");
        this.add("manual.assortedcuisine.chapter.dairy.making",
                "Any milk bucket should work to craft these wonderful Dairy items. So don't worry about it.");

        this.add("manual.assortedcuisine.chapter.dairy.cheese.title", "Cheese");
        this.add("manual.assortedcuisine.chapter.dairy.cheese",
                "A block of cheese cuts into nine pieces, and nine pieces press back into a block.");

        this.add("manual.assortedcuisine.chapter.dairy.hot_cheese.title", "Hot Cheese");
        this.add("manual.assortedcuisine.chapter.dairy.hot_cheese",
                "Melted cheese is the filling a burger wants, and better food than the cold piece was.");
    }

    private void addFoodChapter() {
        this.add("manual.assortedcuisine.chapter.food", "Food");

        this.add("manual.assortedcuisine.chapter.food.eggs.title", "Eggs");
        this.add("manual.assortedcuisine.chapter.food.eggs",
                "Crack eggs into a bowl with some butter, mix them, then cook them in a furnace. Raw eggs are not worth eating, "
                        + "and the game will let you try anyway." + BREAK
                        + "Cooked eggs are filling and heal well, which is most of the reason to keep butter "
                        + "around.");

        this.add("manual.assortedcuisine.chapter.food.sandwiches.title", "Sandwiches");
        this.add("manual.assortedcuisine.chapter.food.sandwiches",
                "Bread cuts into slices, and slices with cheese make a burger that carries a long trip.");

        this.add("manual.assortedcuisine.chapter.food.knife.title", "Knife");
        this.add("manual.assortedcuisine.chapter.food.knife",
                "The knife slices bread and pumpkins. It stays in the crafting grid and wears out a little "
                        + "with every cut.");

        this.add("manual.assortedcuisine.chapter.food.whisk.title", "Whisk");
        this.add("manual.assortedcuisine.chapter.food.whisk",
                "The whisk beats eggs, and wears out the same way the knife does.");
    }

    private void addPiesChapter() {
        this.add("manual.assortedcuisine.chapter.pies", "Pies");

        this.add("manual.assortedcuisine.chapter.pies.ingredients.title", "Ingredients");
        this.add("manual.assortedcuisine.chapter.pies.ingredients",
                "Every pie starts with dough in a pan. Pumpkin slices are the one filling you have to cut "
                        + "yourself.");

        this.add("manual.assortedcuisine.chapter.pies.raw.title", "Filling a Pie");
        this.add("manual.assortedcuisine.chapter.pies.raw",
                "Make an empty pie, then fill it with apple, pumpkin, melon or pork. A raw pie is not food "
                        + "yet.");

        this.add("manual.assortedcuisine.chapter.pies.baking.title", "Baking");
        this.add("manual.assortedcuisine.chapter.pies.baking",
                "A raw pie bakes in a furnace, and the baked pie is a block. Place it down and eat it a slice "
                        + "at a time, the way a cake works.");

        this.add("manual.assortedcuisine.chapter.pies.chocolate.title", "Chocolate Pie");
        this.add("manual.assortedcuisine.chapter.pies.chocolate",
                "Chocolate balls fill a pie too, and it bakes the same way.");
    }

    /** This part's advancements. Keys match the ids {@code KitchenAdvancements} saves. */
    private void addAdvancements() {
        // The Assorted Cuisine root, which every part with advancements writes the same.
        this.add("advancements.assortedcuisine.root.title", "Assorted Cuisine");
        this.add("advancements.assortedcuisine.root.description", "Cook something worth eating");

        this.advancement("cheese", "Say Cheese", "Turn a bucket of milk into a block of cheese");
        this.advancement("butter", "Churn Baby Churn", "Work a butter churn until it gives up its butter");
        this.advancement("sandwich", "Lunch Break", "Build a cheese burger");

        this.advancement("chocolate", "Bar None", "Set hot chocolate into a bar");
        this.advancement("chocolate_cake", "The Cake Is Chocolate", "Bake a chocolate cake");

        this.advancement("pie", "Easy As Pie", "Bake any pie");
        this.advancement("every_pie", "Pie Chart", "Bake all five pies");
    }

    private void advancement(String name, String title, String description) {
        this.add("advancements." + Constants.MOD_ID + "." + name + ".title", title);
        this.add("advancements." + Constants.MOD_ID + "." + name + ".description", description);
    }
}
