package com.grim3212.assorted.dragonfruit.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.api.DragonFruitTags;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class DragonFruitRecipes extends ConditionalRecipeProvider {

    public DragonFruitRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Nothing is conditioned: installing this mod is what turns its recipes on.
    }

    /**
     * Cutting a cactus open for its fruit. It costs the whole cactus, not just a point off the
     * knife: the block has to be broken and spent, or one plant would feed you forever.
     */
    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, DragonFruitItems.DRAGON_FRUIT.get())
                .requires(Items.CACTUS).requires(DragonFruitTags.Items.KNIVES)
                .unlockedBy("has_knife", has(DragonFruitTags.Items.KNIVES)).save(this.output, key("dragon_fruit"));
    }

    /**
     * Recipes are addressed by {@code ResourceKey<Recipe<?>>} rather than a raw id now.
     */
    private static ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
