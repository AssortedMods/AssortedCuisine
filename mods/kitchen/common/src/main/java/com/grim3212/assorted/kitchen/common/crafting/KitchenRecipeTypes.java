package com.grim3212.assorted.kitchen.common.crafting;

import com.grim3212.assorted.kitchen.Constants;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachine;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachineRecipe;
import com.grim3212.assorted.kitchen.api.crafting.KitchenMachineRecipeSerializer;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.EnumMap;
import java.util.Map;

/** A recipe type and a serializer per {@link KitchenMachine}, registered under the machine's name. */
public class KitchenRecipeTypes {

    public static final RegistryProvider<RecipeType<?>> RECIPE_TYPES = RegistryProvider.create(Registries.RECIPE_TYPE, Constants.MOD_ID);
    public static final RegistryProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryProvider.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    private static final Map<KitchenMachine, IRegistryObject<RecipeType<KitchenMachineRecipe>>> TYPES = new EnumMap<>(KitchenMachine.class);

    /** Built eagerly rather than looked up through the registry. They are only codecs, so nothing here reads a registry. */
    private static final Map<KitchenMachine, RecipeSerializer<KitchenMachineRecipe>> SERIALIZERS = new EnumMap<>(KitchenMachine.class);

    static {
        for (KitchenMachine machine : KitchenMachine.values()) {
            RecipeSerializer<KitchenMachineRecipe> serializer = KitchenMachineRecipeSerializer.create(machine);

            SERIALIZERS.put(machine, serializer);
            TYPES.put(machine, RECIPE_TYPES.register(machine.getName(), () -> createRecipeType(machine)));
            RECIPE_SERIALIZERS.register(machine.getName(), () -> serializer);
        }
    }

    public static RecipeType<KitchenMachineRecipe> type(KitchenMachine machine) {
        return TYPES.get(machine).get();
    }

    public static RecipeSerializer<KitchenMachineRecipe> serializer(KitchenMachine machine) {
        return SERIALIZERS.get(machine);
    }

    /** Nothing to ask for: 1.21.1 sends every recipe to the client, so the manual and JEI can say what a machine does. */
    public static void init() {
    }

    private static <T extends Recipe<?>> RecipeType<T> createRecipeType(KitchenMachine machine) {
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return machine.id().toString();
            }
        };
    }
}
