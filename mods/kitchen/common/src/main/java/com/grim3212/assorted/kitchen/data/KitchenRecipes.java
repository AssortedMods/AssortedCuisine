package com.grim3212.assorted.kitchen.data;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.api.KitchenTags;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachine;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.item.KitchenItems;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class KitchenRecipes extends ConditionalRecipeProvider {

    private static final int SMELT_TIME = 200;

    private final HolderGetter<Item> items;

    public KitchenRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        // Nothing is conditioned: installing this mod is what turns its recipes on.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        dairy();
        chocolate();
        pies();
    }

    private void dairy() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, KitchenBlocks.BUTTER_CHURN.get())
                .define('X', ItemTags.PLANKS).define('I', LibCommonTags.Items.RODS_WOODEN)
                .pattern("XIX").pattern("XIX").pattern("XXX")
                .unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output, key("butter_churn"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, KitchenBlocks.CHEESE_MAKER.get())
                .define('X', LibCommonTags.Items.COBBLESTONE).define('I', Items.BUCKET)
                .pattern("X X").pattern("XIX").pattern("XXX")
                .unlockedBy("has_bucket", has(Items.BUCKET)).save(this.output, key("cheese_maker"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, KitchenItems.KNIFE.get())
                .define('X', ItemTags.PLANKS).define('W', LibCommonTags.Items.INGOTS_IRON)
                .pattern("X  ").pattern(" W ").pattern("  W")
                .unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, key("knife"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, KitchenItems.WHISK.get())
                .define('X', LibCommonTags.Items.INGOTS_IRON)
                .pattern("X  ").pattern(" XX").pattern(" X ")
                .unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, key("whisk"));

        // A block of cheese is nine pieces, either way round.
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, KitchenBlocks.CHEESE_BLOCK.get())
                .define('C', KitchenItems.CHEESE.get())
                .pattern("CCC").pattern("CCC").pattern("CCC")
                .unlockedBy("has_cheese", has(KitchenItems.CHEESE.get())).save(this.output, key("cheese_block"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.CHEESE.get(), 9)
                .requires(KitchenBlocks.CHEESE_BLOCK.get())
                .unlockedBy("has_cheese_block", has(KitchenBlocks.CHEESE_BLOCK.get())).save(this.output, key("cheese"));

        // The knife stays in the grid and wears out a point at a time.
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.BREAD_SLICE.get(), 2)
                .requires(Items.BREAD).requires(KitchenTags.Items.KNIVES)
                .unlockedBy("has_knife", has(KitchenTags.Items.KNIVES)).save(this.output, key("bread_slice"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, KitchenItems.CHEESE_BURGER.get())
                .define('A', KitchenTags.Items.FOODS_CHEESE).define('C', KitchenTags.Items.FOODS_BREAD).define('O', Items.COOKED_BEEF)
                .pattern(" C ").pattern("AOA").pattern(" C ")
                .unlockedBy("has_cheese", has(KitchenTags.Items.FOODS_CHEESE)).save(this.output, key("cheese_burger"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, KitchenItems.HOT_CHEESE.get())
                .define('A', KitchenTags.Items.FOODS_CHEESE).define('C', KitchenTags.Items.FOODS_BREAD)
                .pattern(" C ").pattern("AAA").pattern(" C ")
                .unlockedBy("has_cheese", has(KitchenTags.Items.FOODS_CHEESE)).save(this.output, key("hot_cheese"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, KitchenItems.EGGS_UNMIXED.get())
                .define('X', LibCommonTags.Items.EGGS).define('I', KitchenTags.Items.FOODS_BUTTER).define('M', Items.BOWL)
                .pattern("XIX").pattern(" M ")
                .unlockedBy("has_butter", has(KitchenTags.Items.FOODS_BUTTER)).save(this.output, key("eggs_unmixed"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.EGGS_MIXED.get())
                .requires(KitchenItems.EGGS_UNMIXED.get()).requires(KitchenTags.Items.WHISKS)
                .unlockedBy("has_whisk", has(KitchenTags.Items.WHISKS)).save(this.output, key("eggs_mixed"));

        smelt(KitchenItems.EGGS_MIXED.get(), KitchenItems.EGGS_COOKED.get(), 0.35F, "eggs_cooked");

        // What the two dairy machines actually do. 1.12 hardcoded both inside the blocks; as
        // recipes a pack can add a second milk, or a cheese of its own.
        KitchenMachineRecipeBuilder.recipe(KitchenMachine.CHEESE_MAKER, Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.BUCKETS_MILK)), new ItemStackTemplate(KitchenBlocks.CHEESE_BLOCK.get().asItem(), 1))
                .unlockedBy("has_cheese_maker", has(KitchenBlocks.CHEESE_MAKER.get())).save(this.output, key("cheese_making"));

        KitchenMachineRecipeBuilder.recipe(KitchenMachine.BUTTER_CHURN, Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.BUCKETS_MILK)), new ItemStackTemplate(KitchenItems.BUTTER.get(), 2))
                .unlockedBy("has_butter_churn", has(KitchenBlocks.BUTTER_CHURN.get())).save(this.output, key("churning"));
    }

    private void chocolate() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, KitchenItems.MORTAR_AND_PESTLE.get())
                .define('X', LibCommonTags.Items.STONE).define('S', LibCommonTags.Items.RODS_WOODEN)
                .pattern("  S").pattern("XSX").pattern(" X ")
                .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS)).save(this.output, key("mortar_and_pestle"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, KitchenItems.COCOA_DUST.get(), 2)
                .requires(Items.COCOA_BEANS).requires(KitchenTags.Items.MORTARS_AND_PESTLES)
                .unlockedBy("has_mortar_and_pestle", has(KitchenTags.Items.MORTARS_AND_PESTLES)).save(this.output, key("cocoa_dust"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, KitchenItems.CHOCOLATE_BOWL.get())
                .define('X', KitchenItems.COCOA_DUST.get()).define('A', Items.SUGAR).define('B', LibCommonTags.Items.BUCKETS_MILK)
                .pattern(" X ").pattern("XAX").pattern(" B ")
                .unlockedBy("has_cocoa_dust", has(KitchenItems.COCOA_DUST.get())).save(this.output, key("chocolate_bowl"));

        smelt(KitchenItems.CHOCOLATE_BOWL.get(), KitchenItems.HOT_CHOCOLATE.get(), 0.3F, "hot_chocolate");

        // The mould, as a recipe rather than a hardcoded hot chocolate check.
        KitchenMachineRecipeBuilder.recipe(KitchenMachine.CHOCOLATE_MOULD, Ingredient.of(KitchenItems.HOT_CHOCOLATE.get()), new ItemStackTemplate(KitchenItems.CHOCOLATE_BAR.get(), 2))
                .unlockedBy("has_chocolate_bar_mould", has(KitchenBlocks.CHOCOLATE_BAR_MOULD.get())).save(this.output, key("chocolate_moulding"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, KitchenBlocks.CHOCOLATE_BAR_MOULD.get())
                .define('I', LibCommonTags.Items.STONE).define('X', LibCommonTags.Items.COBBLESTONE)
                .pattern(" I ").pattern(" I ").pattern("XXX")
                .unlockedBy("has_hot_chocolate", has(KitchenItems.HOT_CHOCOLATE.get())).save(this.output, key("chocolate_bar_mould"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.CHOCOLATE_BALL.get(), 2)
                .requires(KitchenItems.HOT_CHOCOLATE.get())
                .unlockedBy("has_hot_chocolate", has(KitchenItems.HOT_CHOCOLATE.get())).save(this.output, key("chocolate_ball"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, KitchenBlocks.CHOCOLATE_BLOCK.get())
                .define('X', KitchenItems.CHOCOLATE_BAR.get())
                .pattern("XXX").pattern("XXX").pattern("XXX")
                .unlockedBy("has_chocolate_bar", has(KitchenItems.CHOCOLATE_BAR.get())).save(this.output, key("chocolate_block"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.CHOCOLATE_BAR.get(), 9)
                .requires(KitchenBlocks.CHOCOLATE_BLOCK.get())
                .unlockedBy("has_chocolate_block", has(KitchenBlocks.CHOCOLATE_BLOCK.get())).save(this.output, key("chocolate_bar"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, KitchenItems.WRAPPER.get())
                .requires(Items.PAPER).requires(LibCommonTags.Items.DYES_BLUE)
                .unlockedBy("has_paper", has(Items.PAPER)).save(this.output, key("wrapper"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.CHOCOLATE_BAR_WRAPPED.get())
                .requires(KitchenItems.CHOCOLATE_BAR.get()).requires(KitchenItems.WRAPPER.get())
                .unlockedBy("has_wrapper", has(KitchenItems.WRAPPER.get())).save(this.output, key("chocolate_bar_wrapped"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, KitchenBlocks.CHOCOLATE_CAKE.get())
                .define('B', KitchenItems.CHOCOLATE_BALL.get()).define('X', LibCommonTags.Items.BUCKETS_MILK).define('S', Items.SUGAR).define('E', LibCommonTags.Items.EGGS).define('W', Items.WHEAT)
                .pattern("BXB").pattern("SES").pattern("WWW")
                .unlockedBy("has_chocolate_ball", has(KitchenItems.CHOCOLATE_BALL.get())).save(this.output, key("chocolate_cake"));
    }

    private void pies() {
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.DOUGH.get(), 2)
                .requires(LibCommonTags.Items.BUCKETS_MILK).requires(Items.WHEAT).requires(Items.WHEAT).requires(LibCommonTags.Items.EGGS)
                .unlockedBy("has_wheat", has(Items.WHEAT)).save(this.output, key("dough"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, KitchenItems.PAN.get())
                .define('X', LibCommonTags.Items.STONE)
                .pattern("X X").pattern(" X ")
                .unlockedBy("has_stone", has(LibCommonTags.Items.STONE)).save(this.output, key("pan"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, KitchenItems.PUMPKIN_SLICE.get(), 6)
                .requires(Items.PUMPKIN).requires(KitchenTags.Items.KNIVES)
                .unlockedBy("has_knife", has(KitchenTags.Items.KNIVES)).save(this.output, key("pumpkin_slice"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, KitchenItems.RAW_EMPTY_PIE.get())
                .define('X', KitchenTags.Items.FOODS_DOUGH).define('M', KitchenItems.PAN.get())
                .pattern("X").pattern("M")
                .unlockedBy("has_pan", has(KitchenItems.PAN.get())).save(this.output, key("raw_empty_pie"));

        rawPie(KitchenItems.RAW_APPLE_PIE.get(), Items.APPLE, "raw_apple_pie");
        rawPie(KitchenItems.RAW_MELON_PIE.get(), Items.MELON_SLICE, "raw_melon_pie");
        rawPie(KitchenItems.RAW_PUMPKIN_PIE.get(), KitchenItems.PUMPKIN_SLICE.get(), "raw_pumpkin_pie");
        rawPie(KitchenItems.RAW_CHOCOLATE_PIE.get(), KitchenItems.CHOCOLATE_BALL.get(), "raw_chocolate_pie");
        rawPie(KitchenItems.RAW_PORK_PIE.get(), Items.PORKCHOP, "raw_pork_pie");

        smelt(KitchenItems.RAW_APPLE_PIE.get(), KitchenBlocks.APPLE_PIE.get(), 0.35F, "apple_pie");
        smelt(KitchenItems.RAW_MELON_PIE.get(), KitchenBlocks.MELON_PIE.get(), 0.35F, "melon_pie");
        smelt(KitchenItems.RAW_PUMPKIN_PIE.get(), KitchenBlocks.PUMPKIN_PIE.get(), 0.35F, "pumpkin_pie");
        smelt(KitchenItems.RAW_CHOCOLATE_PIE.get(), KitchenBlocks.CHOCOLATE_PIE.get(), 0.35F, "chocolate_pie");
        smelt(KitchenItems.RAW_PORK_PIE.get(), KitchenBlocks.PORK_PIE.get(), 0.35F, "pork_pie");
    }

    private void rawPie(ItemLike result, ItemLike filling, String name) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, result)
                .define('X', KitchenTags.Items.FOODS_DOUGH).define('M', filling).define('Y', KitchenItems.RAW_EMPTY_PIE.get())
                .pattern(" X ").pattern("MMM").pattern(" Y ")
                .unlockedBy("has_empty_pie", has(KitchenItems.RAW_EMPTY_PIE.get())).save(this.output, key(name));
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
            return new KitchenRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }

    /**
     * Recipes are addressed by {@code ResourceKey<Recipe<?>>} rather than a raw id now.
     */
    private static ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
