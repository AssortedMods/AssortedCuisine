package com.grim3212.assorted.cuisine.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Codecs for {@link CuisineMachineRecipe}. One serializer per {@link CuisineMachine}: the machine
 * is baked into the codec rather than written into the JSON, so a recipe file cannot claim to be
 * for a machine other than the folder it is registered under.
 */
public final class CuisineMachineRecipeSerializer {

    private CuisineMachineRecipeSerializer() {
    }

    public static RecipeSerializer<CuisineMachineRecipe> create(CuisineMachine machine) {
        return new RecipeSerializer<>(codec(machine), streamCodec(machine));
    }

    private static MapCodec<CuisineMachineRecipe> codec(CuisineMachine machine) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(CuisineMachineRecipe::group),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(CuisineMachineRecipe::getIngredient),
                ItemStackTemplate.CODEC.fieldOf("result").forGetter(CuisineMachineRecipe::getResultTemplate),
                Codec.INT.optionalFieldOf("processtime", machine.getDefaultProcessTime()).forGetter(CuisineMachineRecipe::getProcessTime)
        ).apply(instance, (group, ingredient, result, processTime) -> new CuisineMachineRecipe(machine, group, ingredient, result, processTime)));
    }

    private static StreamCodec<RegistryFriendlyByteBuf, CuisineMachineRecipe> streamCodec(CuisineMachine machine) {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, CuisineMachineRecipe::group,
                Ingredient.CONTENTS_STREAM_CODEC, CuisineMachineRecipe::getIngredient,
                ItemStackTemplate.STREAM_CODEC, CuisineMachineRecipe::getResultTemplate,
                ByteBufCodecs.VAR_INT, CuisineMachineRecipe::getProcessTime,
                (group, ingredient, result, processTime) -> new CuisineMachineRecipe(machine, group, ingredient, result, processTime));
    }
}
