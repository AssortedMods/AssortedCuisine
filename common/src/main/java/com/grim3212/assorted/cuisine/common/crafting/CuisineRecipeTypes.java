package com.grim3212.assorted.cuisine.common.crafting;

import com.grim3212.assorted.cuisine.Constants;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipe;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipeSerializer;
import com.grim3212.assorted.lib.crafting.SyncedRecipes;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.EnumMap;
import java.util.Map;

/** A recipe type and a serializer per {@link CuisineMachine}, registered under the machine's name. */
public class CuisineRecipeTypes {

    public static final RegistryProvider<RecipeType<?>> RECIPE_TYPES = RegistryProvider.create(Registries.RECIPE_TYPE, Constants.MOD_ID);
    public static final RegistryProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryProvider.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);
    public static final RegistryProvider<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = RegistryProvider.create(Registries.RECIPE_BOOK_CATEGORY, Constants.MOD_ID);

    /**
     * Every Recipe has to name a book category even when it publishes no display and so never
     * reaches the book. One shared category covers all three machines.
     */
    public static final IRegistryObject<RecipeBookCategory> MACHINE_CATEGORY = RECIPE_BOOK_CATEGORIES.register("machine", RecipeBookCategory::new);

    private static final Map<CuisineMachine, IRegistryObject<RecipeType<CuisineMachineRecipe>>> TYPES = new EnumMap<>(CuisineMachine.class);

    /**
     * Built eagerly rather than looked up through the registry, because {@link SyncedRecipes} wants
     * the serializers during mod construction, before anything is bound. They are only codecs, so
     * nothing here reads a registry.
     */
    private static final Map<CuisineMachine, RecipeSerializer<CuisineMachineRecipe>> SERIALIZERS = new EnumMap<>(CuisineMachine.class);

    static {
        for (CuisineMachine machine : CuisineMachine.values()) {
            RecipeSerializer<CuisineMachineRecipe> serializer = CuisineMachineRecipeSerializer.create(machine);

            SERIALIZERS.put(machine, serializer);
            TYPES.put(machine, RECIPE_TYPES.register(machine.getName(), () -> createRecipeType(machine)));
            RECIPE_SERIALIZERS.register(machine.getName(), () -> serializer);
        }
    }

    public static RecipeType<CuisineMachineRecipe> type(CuisineMachine machine) {
        return TYPES.get(machine).get();
    }

    public static RecipeSerializer<CuisineMachineRecipe> serializer(CuisineMachine machine) {
        return SERIALIZERS.get(machine);
    }

    /**
     * Asks for these to be sent to the client on login. There is no recipe manager client side, and
     * both the manual and JEI need to be able to say what a machine does.
     */
    public static void init() {
        for (CuisineMachine machine : CuisineMachine.values()) {
            SyncedRecipes.require(() -> type(machine), serializer(machine));
        }
    }

    private static <T extends Recipe<?>> RecipeType<T> createRecipeType(CuisineMachine machine) {
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return machine.id().toString();
            }
        };
    }
}
