package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.api.CuisineTags;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.crafting.CuisineConditions.Parts;
import com.grim3212.assorted.cuisine.common.item.CuisineItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class CuisineRecipes extends ConditionalRecipeProvider {

    private static final int SMELT_TIME = 200;

    // The sixteen wools are a ColorCollection in 26.2 rather than sixteen fields on Items.
    private static final Item WHITE_WOOL = Items.WOOL.pick(DyeColor.WHITE);

    private final HolderGetter<Item> items;

    public CuisineRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        this.addConditions(partEnabled(Parts.DAIRY), ids("butter_churn", "cheese_maker", "whisk", "cheese_block", "cheese", "bread_slice",
                "cheese_burger", "hot_cheese", "eggs_unmixed", "eggs_mixed", "eggs_cooked_smelting", "cheese_making", "churning"));
        // Both halves cut with the knife, so either keeps it craftable.
        this.addConditions(or(partEnabled(Parts.DAIRY), partEnabled(Parts.PIES)), ids("knife"));

        this.addConditions(partEnabled(Parts.CHOCOLATE), ids("mortar_and_pestle", "cocoa_dust", "chocolate_bowl", "hot_chocolate_smelting", "chocolate_bar_mould",
                "chocolate_ball", "chocolate_block", "chocolate_bar", "wrapper", "chocolate_bar_wrapped", "chocolate_cake", "chocolate_moulding"));

        this.addConditions(partEnabled(Parts.PIES), ids("dough", "pan", "pumpkin_slice", "raw_empty_pie", "raw_apple_pie", "raw_melon_pie",
                "raw_pumpkin_pie", "raw_pork_pie", "apple_pie_smelting", "melon_pie_smelting", "pumpkin_pie_smelting", "pork_pie_smelting"));
        // The filling is a chocolate ball.
        this.addConditions(and(partEnabled(Parts.PIES), partEnabled(Parts.CHOCOLATE)), ids("raw_chocolate_pie", "chocolate_pie_smelting"));

        this.addConditions(partEnabled(Parts.DRAGON_FRUIT), ids("dragon_fruit"));

        this.addConditions(partEnabled(Parts.HEALTH), ids("sweets", "powered_sugar", "powered_sweets", "bandage", "healthpack", "healthpack_super"));

        this.addConditions(partEnabled(Parts.SODA), ids("soda_bottle", "soda_co2", "soda_carbonated_water", "soda_apple", "soda_golden_apple",
                "soda_slurm", "soda_spiked_orange", "soda_root_beer", "soda_orange", "soda_diamond", "soda_cocoa", "soda_mushroom", "soda_cream_orange"));
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        dairy();
        chocolate();
        dragonFruit();
        pies();
        health();
        soda();
    }

    private void dairy() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, CuisineBlocks.BUTTER_CHURN.get())
                .define('X', ItemTags.PLANKS).define('I', LibCommonTags.Items.RODS_WOODEN)
                .pattern("XIX").pattern("XIX").pattern("XXX")
                .unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output, key("butter_churn"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, CuisineBlocks.CHEESE_MAKER.get())
                .define('X', LibCommonTags.Items.COBBLESTONE).define('I', Items.BUCKET)
                .pattern("X X").pattern("XIX").pattern("XXX")
                .unlockedBy("has_bucket", has(Items.BUCKET)).save(this.output, key("cheese_maker"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, CuisineItems.KNIFE.get())
                .define('X', ItemTags.PLANKS).define('W', LibCommonTags.Items.INGOTS_IRON)
                .pattern("X  ").pattern(" W ").pattern("  W")
                .unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, key("knife"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, CuisineItems.WHISK.get())
                .define('X', LibCommonTags.Items.INGOTS_IRON)
                .pattern("X  ").pattern(" XX").pattern(" X ")
                .unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, key("whisk"));

        // A block of cheese is nine pieces, either way round.
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, CuisineBlocks.CHEESE_BLOCK.get())
                .define('C', CuisineItems.CHEESE.get())
                .pattern("CCC").pattern("CCC").pattern("CCC")
                .unlockedBy("has_cheese", has(CuisineItems.CHEESE.get())).save(this.output, key("cheese_block"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.CHEESE.get(), 9)
                .requires(CuisineBlocks.CHEESE_BLOCK.get())
                .unlockedBy("has_cheese_block", has(CuisineBlocks.CHEESE_BLOCK.get())).save(this.output, key("cheese"));

        // The knife stays in the grid and wears out a point at a time.
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.BREAD_SLICE.get(), 2)
                .requires(Items.BREAD).requires(CuisineTags.Items.KNIVES)
                .unlockedBy("has_knife", has(CuisineTags.Items.KNIVES)).save(this.output, key("bread_slice"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.CHEESE_BURGER.get())
                .define('A', CuisineTags.Items.FOODS_CHEESE).define('C', CuisineTags.Items.FOODS_BREAD).define('O', Items.COOKED_BEEF)
                .pattern(" C ").pattern("AOA").pattern(" C ")
                .unlockedBy("has_cheese", has(CuisineTags.Items.FOODS_CHEESE)).save(this.output, key("cheese_burger"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.HOT_CHEESE.get())
                .define('A', CuisineTags.Items.FOODS_CHEESE).define('C', CuisineTags.Items.FOODS_BREAD)
                .pattern(" C ").pattern("AAA").pattern(" C ")
                .unlockedBy("has_cheese", has(CuisineTags.Items.FOODS_CHEESE)).save(this.output, key("hot_cheese"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.EGGS_UNMIXED.get())
                .define('X', LibCommonTags.Items.EGGS).define('I', CuisineTags.Items.FOODS_BUTTER).define('M', Items.BOWL)
                .pattern("XIX").pattern(" M ")
                .unlockedBy("has_butter", has(CuisineTags.Items.FOODS_BUTTER)).save(this.output, key("eggs_unmixed"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.EGGS_MIXED.get())
                .requires(CuisineItems.EGGS_UNMIXED.get()).requires(CuisineTags.Items.WHISKS)
                .unlockedBy("has_whisk", has(CuisineTags.Items.WHISKS)).save(this.output, key("eggs_mixed"));

        smelt(CuisineItems.EGGS_MIXED.get(), CuisineItems.EGGS_COOKED.get(), 0.35F, "eggs_cooked");

        // What the two dairy machines actually do. 1.12 hardcoded both inside the blocks; as
        // recipes a pack can add a second milk, or a cheese of its own.
        CuisineMachineRecipeBuilder.recipe(CuisineMachine.CHEESE_MAKER, Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.BUCKETS_MILK)), new ItemStackTemplate(CuisineBlocks.CHEESE_BLOCK.get().asItem(), 1))
                .unlockedBy("has_cheese_maker", has(CuisineBlocks.CHEESE_MAKER.get())).save(this.output, key("cheese_making"));

        CuisineMachineRecipeBuilder.recipe(CuisineMachine.BUTTER_CHURN, Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.BUCKETS_MILK)), new ItemStackTemplate(CuisineItems.BUTTER.get(), 2))
                .unlockedBy("has_butter_churn", has(CuisineBlocks.BUTTER_CHURN.get())).save(this.output, key("churning"));
    }

    /**
     * Cutting a cactus open for its fruit. It costs the whole cactus, not just a point off the
     * knife: the block has to be broken and spent, or one plant would feed you forever.
     */
    private void dragonFruit() {
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.DRAGON_FRUIT.get())
                .requires(Items.CACTUS).requires(CuisineTags.Items.KNIVES)
                .unlockedBy("has_knife", has(CuisineTags.Items.KNIVES)).save(this.output, key("dragon_fruit"));
    }

    private void chocolate() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, CuisineItems.MORTAR_AND_PESTLE.get())
                .define('X', LibCommonTags.Items.STONE).define('S', LibCommonTags.Items.RODS_WOODEN)
                .pattern("  S").pattern("XSX").pattern(" X ")
                .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS)).save(this.output, key("mortar_and_pestle"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, CuisineItems.COCOA_DUST.get(), 2)
                .requires(Items.COCOA_BEANS).requires(CuisineTags.Items.MORTARS_AND_PESTLES)
                .unlockedBy("has_mortar_and_pestle", has(CuisineTags.Items.MORTARS_AND_PESTLES)).save(this.output, key("cocoa_dust"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.CHOCOLATE_BOWL.get())
                .define('X', CuisineItems.COCOA_DUST.get()).define('A', Items.SUGAR).define('B', LibCommonTags.Items.BUCKETS_MILK)
                .pattern(" X ").pattern("XAX").pattern(" B ")
                .unlockedBy("has_cocoa_dust", has(CuisineItems.COCOA_DUST.get())).save(this.output, key("chocolate_bowl"));

        smelt(CuisineItems.CHOCOLATE_BOWL.get(), CuisineItems.HOT_CHOCOLATE.get(), 0.3F, "hot_chocolate");

        // The mould, as a recipe rather than a hardcoded hot chocolate check.
        CuisineMachineRecipeBuilder.recipe(CuisineMachine.CHOCOLATE_MOULD, Ingredient.of(CuisineItems.HOT_CHOCOLATE.get()), new ItemStackTemplate(CuisineItems.CHOCOLATE_BAR.get(), 2))
                .unlockedBy("has_chocolate_bar_mould", has(CuisineBlocks.CHOCOLATE_BAR_MOULD.get())).save(this.output, key("chocolate_moulding"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, CuisineBlocks.CHOCOLATE_BAR_MOULD.get())
                .define('I', LibCommonTags.Items.STONE).define('X', LibCommonTags.Items.COBBLESTONE)
                .pattern(" I ").pattern(" I ").pattern("XXX")
                .unlockedBy("has_hot_chocolate", has(CuisineItems.HOT_CHOCOLATE.get())).save(this.output, key("chocolate_bar_mould"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.CHOCOLATE_BALL.get(), 2)
                .requires(CuisineItems.HOT_CHOCOLATE.get())
                .unlockedBy("has_hot_chocolate", has(CuisineItems.HOT_CHOCOLATE.get())).save(this.output, key("chocolate_ball"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, CuisineBlocks.CHOCOLATE_BLOCK.get())
                .define('X', CuisineItems.CHOCOLATE_BAR.get())
                .pattern("XXX").pattern("XXX").pattern("XXX")
                .unlockedBy("has_chocolate_bar", has(CuisineItems.CHOCOLATE_BAR.get())).save(this.output, key("chocolate_block"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.CHOCOLATE_BAR.get(), 9)
                .requires(CuisineBlocks.CHOCOLATE_BLOCK.get())
                .unlockedBy("has_chocolate_block", has(CuisineBlocks.CHOCOLATE_BLOCK.get())).save(this.output, key("chocolate_bar"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, CuisineItems.WRAPPER.get())
                .requires(Items.PAPER).requires(LibCommonTags.Items.DYES_BLUE)
                .unlockedBy("has_paper", has(Items.PAPER)).save(this.output, key("wrapper"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.CHOCOLATE_BAR_WRAPPED.get())
                .requires(CuisineItems.CHOCOLATE_BAR.get()).requires(CuisineItems.WRAPPER.get())
                .unlockedBy("has_wrapper", has(CuisineItems.WRAPPER.get())).save(this.output, key("chocolate_bar_wrapped"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineBlocks.CHOCOLATE_CAKE.get())
                .define('B', CuisineItems.CHOCOLATE_BALL.get()).define('X', LibCommonTags.Items.BUCKETS_MILK).define('S', Items.SUGAR).define('E', LibCommonTags.Items.EGGS).define('W', Items.WHEAT)
                .pattern("BXB").pattern("SES").pattern("WWW")
                .unlockedBy("has_chocolate_ball", has(CuisineItems.CHOCOLATE_BALL.get())).save(this.output, key("chocolate_cake"));
    }

    private void pies() {
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.DOUGH.get(), 2)
                .requires(LibCommonTags.Items.BUCKETS_MILK).requires(Items.WHEAT).requires(Items.WHEAT).requires(LibCommonTags.Items.EGGS)
                .unlockedBy("has_wheat", has(Items.WHEAT)).save(this.output, key("dough"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, CuisineItems.PAN.get())
                .define('X', LibCommonTags.Items.STONE)
                .pattern("X X").pattern(" X ")
                .unlockedBy("has_stone", has(LibCommonTags.Items.STONE)).save(this.output, key("pan"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.PUMPKIN_SLICE.get(), 6)
                .requires(Items.PUMPKIN).requires(CuisineTags.Items.KNIVES)
                .unlockedBy("has_knife", has(CuisineTags.Items.KNIVES)).save(this.output, key("pumpkin_slice"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.RAW_EMPTY_PIE.get())
                .define('X', CuisineTags.Items.FOODS_DOUGH).define('M', CuisineItems.PAN.get())
                .pattern("X").pattern("M")
                .unlockedBy("has_pan", has(CuisineItems.PAN.get())).save(this.output, key("raw_empty_pie"));

        rawPie(CuisineItems.RAW_APPLE_PIE.get(), Items.APPLE, "raw_apple_pie");
        rawPie(CuisineItems.RAW_MELON_PIE.get(), Items.MELON_SLICE, "raw_melon_pie");
        rawPie(CuisineItems.RAW_PUMPKIN_PIE.get(), CuisineItems.PUMPKIN_SLICE.get(), "raw_pumpkin_pie");
        rawPie(CuisineItems.RAW_CHOCOLATE_PIE.get(), CuisineItems.CHOCOLATE_BALL.get(), "raw_chocolate_pie");
        rawPie(CuisineItems.RAW_PORK_PIE.get(), Items.PORKCHOP, "raw_pork_pie");

        smelt(CuisineItems.RAW_APPLE_PIE.get(), CuisineBlocks.APPLE_PIE.get(), 0.35F, "apple_pie");
        smelt(CuisineItems.RAW_MELON_PIE.get(), CuisineBlocks.MELON_PIE.get(), 0.35F, "melon_pie");
        smelt(CuisineItems.RAW_PUMPKIN_PIE.get(), CuisineBlocks.PUMPKIN_PIE.get(), 0.35F, "pumpkin_pie");
        smelt(CuisineItems.RAW_CHOCOLATE_PIE.get(), CuisineBlocks.CHOCOLATE_PIE.get(), 0.35F, "chocolate_pie");
        smelt(CuisineItems.RAW_PORK_PIE.get(), CuisineBlocks.PORK_PIE.get(), 0.35F, "pork_pie");
    }

    private void health() {
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.SWEETS.get(), 8)
                .requires(Items.SUGAR).requires(Items.PAPER)
                .unlockedBy("has_sugar", has(Items.SUGAR)).save(this.output, key("sweets"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, CuisineItems.POWERED_SUGAR.get())
                .requires(LibCommonTags.Items.DUSTS_REDSTONE).requires(Items.SUGAR)
                .unlockedBy("has_redstone", has(LibCommonTags.Items.DUSTS_REDSTONE)).save(this.output, key("powered_sugar"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, CuisineItems.POWERED_SWEETS.get(), 8)
                .requires(CuisineItems.POWERED_SUGAR.get()).requires(Items.PAPER)
                .unlockedBy("has_powered_sugar", has(CuisineItems.POWERED_SUGAR.get())).save(this.output, key("powered_sweets"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, CuisineItems.BANDAGE.get(), 2)
                .define('P', Items.PAPER).define('#', WHITE_WOOL)
                .pattern("P#P")
                .unlockedBy("has_wool", has(WHITE_WOOL)).save(this.output, key("bandage"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, CuisineItems.HEALTHPACK.get())
                .define('#', WHITE_WOOL).define('S', Items.SUGAR)
                .pattern(" # ").pattern("#S#").pattern(" # ")
                .unlockedBy("has_wool", has(WHITE_WOOL)).save(this.output, key("healthpack"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, CuisineItems.HEALTHPACK_SUPER.get())
                .define('#', WHITE_WOOL).define('R', CuisineItems.POWERED_SUGAR.get())
                .pattern(" # ").pattern("#R#").pattern(" # ")
                .unlockedBy("has_powered_sugar", has(CuisineItems.POWERED_SUGAR.get())).save(this.output, key("healthpack_super"));
    }

    private void soda() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, CuisineItems.SODA_BOTTLE.get())
                .define('X', LibCommonTags.Items.GLASS_PANES)
                .pattern("X X").pattern("X X").pattern("XXX")
                .unlockedBy("has_glass_pane", has(LibCommonTags.Items.GLASS_PANES)).save(this.output, key("soda_bottle"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, CuisineItems.SODA_CO2.get(), 4)
                .define('X', LibCommonTags.Items.INGOTS_IRON).define('O', Items.FLINT)
                .pattern(" X ").pattern("XOX").pattern(" X ")
                .unlockedBy("has_flint", has(Items.FLINT)).save(this.output, key("soda_co2"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.SODA_CARBONATED_WATER.get())
                .define('X', CuisineItems.SODA_CO2.get()).define('Y', Items.WATER_BUCKET).define('Z', CuisineItems.SODA_BOTTLE.get())
                .pattern("X").pattern("Y").pattern("Z")
                .unlockedBy("has_soda_bottle", has(CuisineItems.SODA_BOTTLE.get())).save(this.output, key("soda_carbonated_water"));

        // Every flavour is carbonated water plus the thing it tastes of.
        flavour(CuisineItems.SODA_APPLE.get(), Ingredient.of(Items.APPLE), "soda_apple");
        flavour(CuisineItems.SODA_GOLDEN_APPLE.get(), Ingredient.of(Items.GOLDEN_APPLE), "soda_golden_apple");
        flavour(CuisineItems.SODA_SLURM.get(), Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.SLIMEBALLS)), "soda_slurm");
        flavour(CuisineItems.SODA_SPIKED_ORANGE.get(), Ingredient.of(Items.JACK_O_LANTERN), "soda_spiked_orange");
        flavour(CuisineItems.SODA_ROOT_BEER.get(), Ingredient.of(Items.WHEAT_SEEDS), "soda_root_beer");
        flavour(CuisineItems.SODA_ORANGE.get(), Ingredient.of(Items.PUMPKIN), "soda_orange");
        flavour(CuisineItems.SODA_DIAMOND.get(), Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.GEMS_DIAMOND)), "soda_diamond");
        flavour(CuisineItems.SODA_COCOA.get(), Ingredient.of(Items.COCOA_BEANS), "soda_cocoa");
        flavour(CuisineItems.SODA_MUSHROOM.get(), Ingredient.of(Items.RED_MUSHROOM), "soda_mushroom");

        // Cream orange is the one built on another soda rather than on plain carbonated water.
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, CuisineItems.SODA_CREAM_ORANGE.get())
                .define('X', LibCommonTags.Items.BUCKETS_MILK).define('Y', CuisineItems.SODA_ORANGE.get())
                .pattern("X").pattern("Y")
                .unlockedBy("has_soda_orange", has(CuisineItems.SODA_ORANGE.get())).save(this.output, key("soda_cream_orange"));
    }

    private void flavour(ItemLike result, Ingredient flavour, String name) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, result)
                .define('X', flavour).define('Y', CuisineItems.SODA_CARBONATED_WATER.get())
                .pattern("X").pattern("Y")
                .unlockedBy("has_carbonated_water", has(CuisineItems.SODA_CARBONATED_WATER.get())).save(this.output, key(name));
    }

    private void rawPie(ItemLike result, ItemLike filling, String name) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, result)
                .define('X', CuisineTags.Items.FOODS_DOUGH).define('M', filling).define('Y', CuisineItems.RAW_EMPTY_PIE.get())
                .pattern(" X ").pattern("MMM").pattern(" Y ")
                .unlockedBy("has_empty_pie", has(CuisineItems.RAW_EMPTY_PIE.get())).save(this.output, key(name));
    }

    private void smelt(ItemLike input, ItemLike result, float experience, String name) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, CookingBookCategory.FOOD, result, experience, SMELT_TIME)
                .unlockedBy("has_ingredient", has(input)).save(this.output, key(name + "_smelting"));
    }

    /**
     * Recipe providers are not data providers any more - a {@link RecipeProvider.Runner} owns the
     * file writing and builds a fresh provider around the {@link RecipeOutput} it hands out. This is
     * what the loader datagen entry points register.
     */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new CuisineRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }

    private static Identifier[] ids(String... paths) {
        Identifier[] ids = new Identifier[paths.length];
        for (int i = 0; i < paths.length; i++) {
            ids[i] = Identifier.fromNamespaceAndPath(Constants.MOD_ID, paths[i]);
        }
        return ids;
    }

    /**
     * Recipes are addressed by {@code ResourceKey<Recipe<?>>} rather than a raw id now.
     */
    private static ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
