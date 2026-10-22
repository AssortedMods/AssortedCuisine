package com.grim3212.assorted.health.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class HealthRecipes extends ConditionalRecipeProvider {

    // The sixteen wools are a ColorCollection in 26.2 rather than sixteen fields on Items.
    private static final Item WHITE_WOOL = Items.WOOL.pick(DyeColor.WHITE);

    public HealthRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Nothing is conditioned: installing this mod is what turns its recipes on.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, HealthItems.SWEETS.get(), 8)
                .requires(Items.SUGAR).requires(Items.PAPER)
                .unlockedBy("has_sugar", has(Items.SUGAR)).save(this.output, key("sweets"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, HealthItems.POWERED_SUGAR.get())
                .requires(LibCommonTags.Items.DUSTS_REDSTONE).requires(Items.SUGAR)
                .unlockedBy("has_redstone", has(LibCommonTags.Items.DUSTS_REDSTONE)).save(this.output, key("powered_sugar"));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, HealthItems.POWERED_SWEETS.get(), 8)
                .requires(HealthItems.POWERED_SUGAR.get()).requires(Items.PAPER)
                .unlockedBy("has_powered_sugar", has(HealthItems.POWERED_SUGAR.get())).save(this.output, key("powered_sweets"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, HealthItems.BANDAGE.get(), 2)
                .define('P', Items.PAPER).define('#', WHITE_WOOL)
                .pattern("P#P")
                .unlockedBy("has_wool", has(WHITE_WOOL)).save(this.output, key("bandage"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, HealthItems.HEALTHPACK.get())
                .define('#', WHITE_WOOL).define('S', Items.SUGAR)
                .pattern(" # ").pattern("#S#").pattern(" # ")
                .unlockedBy("has_wool", has(WHITE_WOOL)).save(this.output, key("healthpack"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, HealthItems.HEALTHPACK_SUPER.get())
                .define('#', WHITE_WOOL).define('R', HealthItems.POWERED_SUGAR.get())
                .pattern(" # ").pattern("#R#").pattern(" # ")
                .unlockedBy("has_powered_sugar", has(HealthItems.POWERED_SUGAR.get())).save(this.output, key("healthpack_super"));
    }

    /**
     * Recipes are addressed by {@code ResourceKey<Recipe<?>>} rather than a raw id now.
     */
    private static ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
