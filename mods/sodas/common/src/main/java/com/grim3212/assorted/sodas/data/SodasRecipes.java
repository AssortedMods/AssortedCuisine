package com.grim3212.assorted.sodas.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class SodasRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public SodasRecipes(HolderLookup.Provider registries, RecipeOutput output) {
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

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, SodasItems.SODA_BOTTLE.get())
                .define('X', LibCommonTags.Items.GLASS_PANES)
                .pattern("X X").pattern("X X").pattern("XXX")
                .unlockedBy("has_glass_pane", has(LibCommonTags.Items.GLASS_PANES)).save(this.output, key("soda_bottle"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, SodasItems.SODA_CO2.get(), 4)
                .define('X', LibCommonTags.Items.INGOTS_IRON).define('O', Items.FLINT)
                .pattern(" X ").pattern("XOX").pattern(" X ")
                .unlockedBy("has_flint", has(Items.FLINT)).save(this.output, key("soda_co2"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, SodasItems.SODA_CARBONATED_WATER.get())
                .define('X', SodasItems.SODA_CO2.get()).define('Y', Items.WATER_BUCKET).define('Z', SodasItems.SODA_BOTTLE.get())
                .pattern("X").pattern("Y").pattern("Z")
                .unlockedBy("has_soda_bottle", has(SodasItems.SODA_BOTTLE.get())).save(this.output, key("soda_carbonated_water"));

        // Every flavor is carbonated water plus the thing it tastes of.
        flavor(SodasItems.SODA_APPLE.get(), Ingredient.of(Items.APPLE), "soda_apple");
        flavor(SodasItems.SODA_GOLDEN_APPLE.get(), Ingredient.of(Items.GOLDEN_APPLE), "soda_golden_apple");
        flavor(SodasItems.SODA_SLURM.get(), Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.SLIMEBALLS)), "soda_slurm");
        flavor(SodasItems.SODA_SPIKED_ORANGE.get(), Ingredient.of(Items.JACK_O_LANTERN), "soda_spiked_orange");
        flavor(SodasItems.SODA_ROOT_BEER.get(), Ingredient.of(Items.WHEAT_SEEDS), "soda_root_beer");
        flavor(SodasItems.SODA_ORANGE.get(), Ingredient.of(Items.PUMPKIN), "soda_orange");
        flavor(SodasItems.SODA_DIAMOND.get(), Ingredient.of(this.items.getOrThrow(LibCommonTags.Items.GEMS_DIAMOND)), "soda_diamond");
        flavor(SodasItems.SODA_COCOA.get(), Ingredient.of(Items.COCOA_BEANS), "soda_cocoa");
        flavor(SodasItems.SODA_MUSHROOM.get(), Ingredient.of(Items.RED_MUSHROOM), "soda_mushroom");

        // Cream orange is the one built on another soda rather than on plain carbonated water.
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, SodasItems.SODA_CREAM_ORANGE.get())
                .define('X', LibCommonTags.Items.BUCKETS_MILK).define('Y', SodasItems.SODA_ORANGE.get())
                .pattern("X").pattern("Y")
                .unlockedBy("has_soda_orange", has(SodasItems.SODA_ORANGE.get())).save(this.output, key("soda_cream_orange"));
    }

    private void flavor(ItemLike result, Ingredient flavor, String name) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, result)
                .define('X', flavor).define('Y', SodasItems.SODA_CARBONATED_WATER.get())
                .pattern("X").pattern("Y")
                .unlockedBy("has_carbonated_water", has(SodasItems.SODA_CARBONATED_WATER.get())).save(this.output, key(name));
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
            return new SodasRecipes(registries, output);
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
