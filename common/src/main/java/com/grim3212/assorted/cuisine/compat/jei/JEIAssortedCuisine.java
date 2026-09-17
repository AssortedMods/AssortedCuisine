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
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;

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

    static {
        for (CuisineMachine machine : CuisineMachine.values()) {
            TYPES.put(machine, IRecipeType.create(Constants.MOD_ID, machine.getName(), CuisineMachineRecipe.class));
        }
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
     * login packet for the types {@code CuisineRecipeTypes} asked to have sent.
     */
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (CuisineMachine machine : CuisineMachine.values()) {
            List<CuisineMachineRecipe> recipes = SyncedRecipes.byType(CuisineRecipeTypes.type(machine)).stream()
                    .map(RecipeHolder::value)
                    .toList();

            registration.addRecipes(TYPES.get(machine), recipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (CuisineMachine machine : CuisineMachine.values()) {
            registration.addCraftingStation(TYPES.get(machine), block(machine));
        }
    }

    private static Block block(CuisineMachine machine) {
        return switch (machine) {
            case CHEESE_MAKER -> CuisineBlocks.CHEESE_MAKER.get();
            case BUTTER_CHURN -> CuisineBlocks.BUTTER_CHURN.get();
            case CHOCOLATE_MOULD -> CuisineBlocks.CHOCOLATE_BAR_MOULD.get();
        };
    }
}
