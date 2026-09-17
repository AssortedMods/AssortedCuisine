package com.grim3212.assorted.cuisine.data;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/** Builds one {@link CuisineMachineRecipe} for data generation. */
public class CuisineMachineRecipeBuilder implements RecipeBuilder {

    private final CuisineMachine machine;
    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    private final int processTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private @Nullable String group;

    private CuisineMachineRecipeBuilder(CuisineMachine machine, Ingredient ingredient, ItemStackTemplate result, int processTime) {
        this.machine = machine;
        this.ingredient = ingredient;
        this.result = result;
        this.processTime = processTime;
    }

    /** Takes the machine's own default time, which is what every recipe in this mod wants. */
    public static CuisineMachineRecipeBuilder recipe(CuisineMachine machine, Ingredient ingredient, ItemStackTemplate result) {
        return new CuisineMachineRecipeBuilder(machine, ingredient, result, machine.getDefaultProcessTime());
    }

    public static CuisineMachineRecipeBuilder recipe(CuisineMachine machine, Ingredient ingredient, ItemStackTemplate result, int processTime) {
        return new CuisineMachineRecipeBuilder(machine, ingredient, result, processTime);
    }

    @Override
    public CuisineMachineRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public CuisineMachineRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id.identifier());
        }

        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        CuisineMachineRecipe recipe = new CuisineMachineRecipe(this.machine, this.group == null ? "" : this.group, this.ingredient, this.result, this.processTime);
        output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/")));
    }
}
