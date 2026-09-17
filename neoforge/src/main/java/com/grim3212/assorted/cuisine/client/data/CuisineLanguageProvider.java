package com.grim3212.assorted.cuisine.client.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block or item whose name is its id in title case needs no
 * line here (see {@link LibLanguageProvider}); these are the names that read differently, and every
 * key that is not a name.
 */
public class CuisineLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public CuisineLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedcuisine", "Assorted Cuisine");

        // Names that are not just the id in title case.
        this.add("item.assortedcuisine.eggs_unmixed", "Cracked Eggs");
        this.add("item.assortedcuisine.eggs_mixed", "Mixed Eggs");
        this.add("item.assortedcuisine.eggs_cooked", "Scrambled Eggs");
        this.add("item.assortedcuisine.powered_sugar", "Powered Sugar");
        this.add("item.assortedcuisine.powered_sweets", "Powered Sweets");
        this.add("item.assortedcuisine.sweets", "Sugar Sweets");
        this.add("item.assortedcuisine.healthpack", "Health Pack");
        this.add("item.assortedcuisine.healthpack_super", "Super Health Pack");
        this.add("item.assortedcuisine.hot_cheese", "Cheese Sandwich");
        this.add("item.assortedcuisine.pan", "Pie Pan");
        this.add("item.assortedcuisine.mortar_and_pestle", "Mortar and Pestle");
        this.add("item.assortedcuisine.raw_empty_pie", "Unbaked Pie Shell");
        this.add("item.assortedcuisine.raw_apple_pie", "Unbaked Apple Pie");
        this.add("item.assortedcuisine.raw_melon_pie", "Unbaked Melon Pie");
        this.add("item.assortedcuisine.raw_pumpkin_pie", "Unbaked Pumpkin Pie");
        this.add("item.assortedcuisine.raw_chocolate_pie", "Unbaked Chocolate Pie");
        this.add("item.assortedcuisine.raw_pork_pie", "Unbaked Pork Pie");
        this.add("item.assortedcuisine.chocolate_bowl", "Bowl of Chocolate");
        this.add("item.assortedcuisine.hot_chocolate", "Hot Chocolate");
        this.add("item.assortedcuisine.soda_bottle", "Soda Bottle");
        this.add("item.assortedcuisine.soda_co2", "CO2 Canister");
        this.add("item.assortedcuisine.soda_carbonated_water", "Carbonated Water");
        this.add("item.assortedcuisine.soda_apple", "Apple Soda");
        this.add("item.assortedcuisine.soda_golden_apple", "Golden Apple Soda");
        this.add("item.assortedcuisine.soda_diamond", "Diamond Soda");
        this.add("item.assortedcuisine.soda_cocoa", "Cocoa Soda");
        this.add("item.assortedcuisine.soda_orange", "Orange Soda");
        this.add("item.assortedcuisine.soda_cream_orange", "Orange Cream Soda");
        this.add("item.assortedcuisine.soda_spiked_orange", "Spiked Orange Soda");
        this.add("item.assortedcuisine.soda_root_beer", "Root Beer");
        this.add("item.assortedcuisine.soda_mushroom", "Mushroom Soda");
        this.add("item.assortedcuisine.soda_slurm", "Slurm");

        // The soda and healing tooltips, so thirteen near-identical bottles can be told apart.
        this.add("tooltip.assortedcuisine.made_in", "Made in %s");
        this.add("tooltip.assortedcuisine.restores", "Restores %s hearts");
        this.add("tooltip.assortedcuisine.hurts", "Costs %s hearts");

        this.add("death.attack.assortedcuisine.spiked_soda", "%1$s drank a spiked orange soda");
        this.add("death.attack.assortedcuisine.spiked_soda.player", "%1$s drank a spiked orange soda given to them by %2$s");

        this.add("tag.item.assortedcuisine.pies", "Pies");
        this.add("tag.item.assortedcuisine.sodas", "Sodas");
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

    /** The chapters in {@code assets/assortedcuisine/manual} name these keys. */
    private void addManual() {
        this.add("manual.assortedcuisine.title", "Assorted Cuisine");
        this.add("manual.assortedcuisine.description",
                "Cocoa, chocolate, dairy and everything else this mod gives you to cook, bottle and eat.");

        this.addChocolateChapter();
        this.addDairyChapter();
        this.addFoodChapter();
        this.addDragonFruitChapter();
        this.addPiesChapter();
        this.addSodaChapter();
        this.addSugarChapter();
        this.addHealthChapter();
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

        this.add("manual.assortedcuisine.chapter.chocolate.mould.title", "Chocolate Bar Mould");
        this.add("manual.assortedcuisine.chapter.chocolate.mould",
                "Place the mould and right click it with hot chocolate to pour." + BREAK
                        + "It steams while it sets, and every block of ice or snow packed against its sides "
                        + "cools it faster." + BREAK
                        + "Once it has set, right click the mould for the bars.");

        this.add("manual.assortedcuisine.chapter.chocolate.moulding.title", "Setting Bars");
        this.add("manual.assortedcuisine.chapter.chocolate.moulding",
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
                "Crack eggs into a pan, mix them, then cook them in a furnace. Raw eggs are not worth eating, "
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

    private void addDragonFruitChapter() {
        this.add("manual.assortedcuisine.chapter.dragon_fruit", "Dragon Fruit");

        this.add("manual.assortedcuisine.chapter.dragon_fruit.cactus.title", "Dragon Fruit");
        this.add("manual.assortedcuisine.chapter.dragon_fruit.cactus",
                "Cacti sometimes drop dragon fruit when they break. It is a small meal.");

        this.add("manual.assortedcuisine.chapter.dragon_fruit.cutting.title", "Cutting One Open");
        this.add("manual.assortedcuisine.chapter.dragon_fruit.cutting",
                "Waiting on the drop is slow, so cut one open instead. A cactus and a knife give a fruit every time."); 
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

    private void addSodaChapter() {
        this.add("manual.assortedcuisine.chapter.soda", "Soda");

        this.add("manual.assortedcuisine.chapter.soda.carbonated.title", "Carbonated Water");
        this.add("manual.assortedcuisine.chapter.soda.carbonated",
                "Every soda starts the same way: a soda bottle, some carbon dioxide, and water to carbonate. "
                        + "Get that far and the flavour is the easy part.");

        this.add("manual.assortedcuisine.chapter.soda.types.title", "Flavours");
        this.add("manual.assortedcuisine.chapter.soda.types",
                "Ten flavours, and they do not heal the same. Slurm and root beer are light, apple and the two "
                        + "oranges are ordinary, and cocoa, golden apple and diamond are worth saving." + BREAK
                        + "Spiked orange is the exception. It hurts whoever drinks it, which is the whole point "
                        + "of handing one to somebody else.");
    }

    private void addSugarChapter() {
        this.add("manual.assortedcuisine.chapter.sugar", "Sweets");

        this.add("manual.assortedcuisine.chapter.sugar.sweets.title", "Sweets");
        this.add("manual.assortedcuisine.chapter.sugar.sweets",
                "Powdered sugar makes sweets, and powdered sweets make the stronger kind. Neither is a meal, "
                        + "but both stack deep and eat quickly." + BREAK
                        + "Powdered sweets are also what the super health pack is built on.");
    }

    private void addHealthChapter() {
        this.add("manual.assortedcuisine.chapter.health", "Healing");

        this.add("manual.assortedcuisine.chapter.health.packs.title", "Bandages and Packs");
        this.add("manual.assortedcuisine.chapter.health.packs",
                "Food heals slowly and only while you are fed. These three heal directly, which is what you "
                        + "want halfway through a fight - but each takes a moment to apply, and being hit "
                        + "interrupts it." + BREAK
                        + "The bandage is the cheap one, the health pack the middle, and the super pack needs "
                        + "powdered sweets from the previous chapter.");
    }

    /** The advancement tab. Keys match the ids {@code CuisineAdvancements} saves. */
    private void addAdvancements() {
        this.advancement("root", "Assorted Cuisine", "Cook something worth eating");

        this.advancement("cheese", "Say Cheese", "Turn a bucket of milk into a block of cheese");
        this.advancement("butter", "Churn Baby Churn", "Work a butter churn until it gives up its butter");
        this.advancement("sandwich", "Lunch Break", "Build a cheese burger");

        this.advancement("chocolate", "Bar None", "Set hot chocolate into a bar");
        this.advancement("chocolate_cake", "The Cake Is Chocolate", "Bake a chocolate cake");

        this.advancement("pie", "Easy As Pie", "Bake any pie");
        this.advancement("every_pie", "Pie Chart", "Bake all five pies");

        this.advancement("soda", "Fizzy Lifting", "Bottle your first soda");
        this.advancement("every_soda", "Taste Test", "Collect all ten drinkable flavours");
        this.advancement("spiked", "Hold My Drink", "Find out what is in a spiked orange soda");

        this.advancement("dragon_fruit", "Fruit of the Desert", "Take dragon fruit from a cactus");
        this.advancement("field_medic", "Field Medic", "Build a super health pack");
    }

    private void advancement(String name, String title, String description) {
        this.add("advancements." + Constants.MOD_ID + "." + name + ".title", title);
        this.add("advancements." + Constants.MOD_ID + "." + name + ".description", description);
    }
}
