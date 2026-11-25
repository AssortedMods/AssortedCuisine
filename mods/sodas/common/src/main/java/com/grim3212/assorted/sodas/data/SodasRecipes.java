package com.grim3212.assorted.sodas.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.sodas.Constants;
import com.grim3212.assorted.sodas.common.item.SodasItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class SodasRecipes extends ConditionalRecipeProvider {

    public SodasRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Nothing is conditioned: installing this mod is what turns its recipes on.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SodasItems.SODA_BOTTLE.get())
                .define('X', LibCommonTags.Items.GLASS_PANES)
                .pattern("X X").pattern("X X").pattern("XXX")
                .unlockedBy("has_glass_pane", has(LibCommonTags.Items.GLASS_PANES)).save(this.output, key("soda_bottle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SodasItems.SODA_CO2.get(), 4)
                .define('X', LibCommonTags.Items.INGOTS_IRON).define('O', Items.FLINT)
                .pattern(" X ").pattern("XOX").pattern(" X ")
                .unlockedBy("has_flint", has(Items.FLINT)).save(this.output, key("soda_co2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, SodasItems.SODA_CARBONATED_WATER.get())
                .define('X', SodasItems.SODA_CO2.get()).define('Y', Items.WATER_BUCKET).define('Z', SodasItems.SODA_BOTTLE.get())
                .pattern("X").pattern("Y").pattern("Z")
                .unlockedBy("has_soda_bottle", has(SodasItems.SODA_BOTTLE.get())).save(this.output, key("soda_carbonated_water"));

        // Every flavor is carbonated water plus the thing it tastes of.
        flavor(SodasItems.SODA_APPLE.get(), Ingredient.of(Items.APPLE), "soda_apple");
        flavor(SodasItems.SODA_GOLDEN_APPLE.get(), Ingredient.of(Items.GOLDEN_APPLE), "soda_golden_apple");
        flavor(SodasItems.SODA_SLURM.get(), Ingredient.of(LibCommonTags.Items.SLIMEBALLS), "soda_slurm");
        flavor(SodasItems.SODA_SPIKED_ORANGE.get(), Ingredient.of(Items.JACK_O_LANTERN), "soda_spiked_orange");
        flavor(SodasItems.SODA_ROOT_BEER.get(), Ingredient.of(Items.WHEAT_SEEDS), "soda_root_beer");
        flavor(SodasItems.SODA_ORANGE.get(), Ingredient.of(Items.PUMPKIN), "soda_orange");
        flavor(SodasItems.SODA_DIAMOND.get(), Ingredient.of(LibCommonTags.Items.GEMS_DIAMOND), "soda_diamond");
        flavor(SodasItems.SODA_COCOA.get(), Ingredient.of(Items.COCOA_BEANS), "soda_cocoa");
        flavor(SodasItems.SODA_MUSHROOM.get(), Ingredient.of(Items.RED_MUSHROOM), "soda_mushroom");

        // Cream orange is the one built on another soda rather than on plain carbonated water.
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, SodasItems.SODA_CREAM_ORANGE.get())
                .define('X', LibCommonTags.Items.BUCKETS_MILK).define('Y', SodasItems.SODA_ORANGE.get())
                .pattern("X").pattern("Y")
                .unlockedBy("has_soda_orange", has(SodasItems.SODA_ORANGE.get())).save(this.output, key("soda_cream_orange"));
    }

    private void flavor(ItemLike result, Ingredient flavor, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, result)
                .define('X', flavor).define('Y', SodasItems.SODA_CARBONATED_WATER.get())
                .pattern("X").pattern("Y")
                .unlockedBy("has_carbonated_water", has(SodasItems.SODA_CARBONATED_WATER.get())).save(this.output, key(name));
    }

    private static ResourceLocation key(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
