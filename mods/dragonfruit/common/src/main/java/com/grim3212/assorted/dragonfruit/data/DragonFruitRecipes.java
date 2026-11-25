package com.grim3212.assorted.dragonfruit.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.api.DragonFruitTags;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class DragonFruitRecipes extends ConditionalRecipeProvider {

    public DragonFruitRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, DragonFruitItems.DRAGON_FRUIT.get())
                .requires(Items.CACTUS).requires(DragonFruitTags.Items.KNIVES)
                .unlockedBy("has_knife", has(DragonFruitTags.Items.KNIVES)).save(this.output, key("dragon_fruit"));
    }

    private static ResourceLocation key(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
