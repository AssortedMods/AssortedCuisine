package com.grim3212.assorted.cuisine.compat.jei;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipe;
import com.grim3212.assorted.cuisine.common.block.CuisineBlocks;
import com.grim3212.assorted.cuisine.common.crafting.CuisineRecipeTypes;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Shows what the three machines do. Without this the only place a player could find out that a
 * cheese maker takes a milk bucket was the manual - the processes are not crafting recipes, so
 * nothing in the vanilla UI mentions them.
 */
@JeiPlugin
public class JEIAssortedCuisine implements IModPlugin {

    private static final Identifier PLUGIN_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "assets/assortedcuisine");

    private static final Map<CuisineMachine, IRecipeType<CuisineMachineRecipe>> TYPES = new EnumMap<>(CuisineMachine.class);

    /**
     * What JEI has been handed, so a later sync knows what to take back out. Only ever touched from
     * the client thread: plugin loading and packet handling both run there.
     */
    private static final Map<CuisineMachine, List<CuisineMachineRecipe>> SHOWN = new EnumMap<>(CuisineMachine.class);

    /** Replaced wholesale on each sync, so identity spots a fresh one. */
    private static RecipeMap shownFrom = RecipeMap.EMPTY;

    private static @Nullable IJeiRuntime runtime;

    static {
        for (CuisineMachine machine : CuisineMachine.values()) {
            TYPES.put(machine, IRecipeType.create(Constants.MOD_ID, machine.getName(), CuisineMachineRecipe.class));
            SHOWN.put(machine, List.of());
        }

        SyncedRecipes.addUpdateListener(JEIAssortedCuisine::onRecipesUpdated);
    }

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        for (CuisineMachine machine : CuisineMachine.values()) {
            registration.addRecipeCategories(new CuisineMachineRecipeCategory(guiHelper, TYPES.get(machine), machine, block(machine)));
        }
    }

    /**
     * From {@link SyncedRecipes}: there is no recipe manager on the client, so these arrive with the
     * login packet. They routinely arrive <em>after</em> this runs, which is why the listing is
     * redone in {@link #onRecipesUpdated()} rather than only being read once here.
     */
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        shownFrom = SyncedRecipes.recipes();

        for (CuisineMachine machine : CuisineMachine.values()) {
            List<CuisineMachineRecipe> recipes = recipesFor(machine);

            SHOWN.put(machine, recipes);
            registration.addRecipes(TYPES.get(machine), recipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (CuisineMachine machine : CuisineMachine.values()) {
            registration.addCraftingStation(TYPES.get(machine), block(machine));
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        // The sync may have landed between registerRecipes and here, in which case what JEI holds
        // is already out of date.
        onRecipesUpdated();
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
    }

    /**
     * Replaces the listed recipes after a sync. They can arrive after {@link #registerRecipes}, and
     * a reload sends them again; the lists are replaced wholesale, so identity spots a new set.
     */
    private static void onRecipesUpdated() {
        IJeiRuntime jeiRuntime = runtime;

        if (jeiRuntime == null) {
            return;
        }

        RecipeMap current = SyncedRecipes.recipes();

        if (current == shownFrom) {
            return;
        }

        shownFrom = current;
        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();

        for (CuisineMachine machine : CuisineMachine.values()) {
            recipeManager.hideRecipes(TYPES.get(machine), SHOWN.get(machine));

            List<CuisineMachineRecipe> recipes = recipesFor(machine);
            SHOWN.put(machine, recipes);
            recipeManager.addRecipes(TYPES.get(machine), recipes);
        }
    }

    private static List<CuisineMachineRecipe> recipesFor(CuisineMachine machine) {
        return SyncedRecipes.byType(CuisineRecipeTypes.type(machine)).stream()
                .map(RecipeHolder::value)
                .toList();
    }

    private static Block block(CuisineMachine machine) {
        return switch (machine) {
            case CHEESE_MAKER -> CuisineBlocks.CHEESE_MAKER.get();
            case BUTTER_CHURN -> CuisineBlocks.BUTTER_CHURN.get();
            case CHOCOLATE_MOULD -> CuisineBlocks.CHOCOLATE_BAR_MOULD.get();
        };
    }
}
