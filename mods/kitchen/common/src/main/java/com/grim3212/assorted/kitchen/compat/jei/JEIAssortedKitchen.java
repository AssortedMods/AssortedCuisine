package com.grim3212.assorted.kitchen.compat.jei;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachine;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachineRecipe;
import com.grim3212.assorted.kitchen.common.block.KitchenBlocks;
import com.grim3212.assorted.kitchen.common.crafting.KitchenRecipeTypes;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Shows what the three machines do. Without this the only place a player could find out that a
 * cheese maker takes a milk bucket was the manual - the processes are not crafting recipes, so
 * nothing in the vanilla UI mentions them.
 */
@JeiPlugin
public class JEIAssortedKitchen implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jei");

    private static final Map<KitchenMachine, RecipeType<KitchenMachineRecipe>> TYPES = new EnumMap<>(KitchenMachine.class);

    /**
     * What JEI has been handed, so a later sync knows what to take back out. Only ever touched from
     * the client thread: plugin loading and packet handling both run there.
     */
    private static final Map<KitchenMachine, List<KitchenMachineRecipe>> SHOWN = new EnumMap<>(KitchenMachine.class);

    /** Replaced wholesale on each sync, so identity spots a fresh one. */
    private static Collection<RecipeHolder<?>> shownFrom = SyncedRecipes.recipes();

    private static @Nullable IJeiRuntime runtime;

    static {
        for (KitchenMachine machine : KitchenMachine.values()) {
            TYPES.put(machine, RecipeType.create(Constants.MOD_ID, machine.getName(), KitchenMachineRecipe.class));
            SHOWN.put(machine, List.of());
        }

        SyncedRecipes.addUpdateListener(JEIAssortedKitchen::onRecipesUpdated);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        for (KitchenMachine machine : KitchenMachine.values()) {
            registration.addRecipeCategories(new KitchenMachineRecipeCategory(guiHelper, TYPES.get(machine), machine, block(machine)));
        }
    }

    /**
     * From {@link SyncedRecipes}, which every loader fills from the recipes the server sent on login. They routinely arrive <em>after</em> this runs, which is why the listing is
     * redone in {@link #onRecipesUpdated()} rather than only being read once here.
     */
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        shownFrom = SyncedRecipes.recipes();

        for (KitchenMachine machine : KitchenMachine.values()) {
            List<KitchenMachineRecipe> recipes = recipesFor(machine);

            SHOWN.put(machine, recipes);
            registration.addRecipes(TYPES.get(machine), recipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (KitchenMachine machine : KitchenMachine.values()) {
            registration.addRecipeCatalysts(TYPES.get(machine), block(machine));
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

        Collection<RecipeHolder<?>> current = SyncedRecipes.recipes();

        if (current == shownFrom) {
            return;
        }

        shownFrom = current;
        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();

        for (KitchenMachine machine : KitchenMachine.values()) {
            recipeManager.hideRecipes(TYPES.get(machine), SHOWN.get(machine));

            List<KitchenMachineRecipe> recipes = recipesFor(machine);
            SHOWN.put(machine, recipes);
            recipeManager.addRecipes(TYPES.get(machine), recipes);
        }
    }

    private static List<KitchenMachineRecipe> recipesFor(KitchenMachine machine) {
        return SyncedRecipes.byType(KitchenRecipeTypes.type(machine)).stream()
                .map(RecipeHolder::value)
                .toList();
    }

    private static Block block(KitchenMachine machine) {
        return switch (machine) {
            case CHEESE_MAKER -> KitchenBlocks.CHEESE_MAKER.get();
            case BUTTER_CHURN -> KitchenBlocks.BUTTER_CHURN.get();
            case CHOCOLATE_MOLD -> KitchenBlocks.CHOCOLATE_BAR_MOLD.get();
        };
    }
}
