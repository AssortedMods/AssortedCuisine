package com.grim3212.assorted.kitchen.api.crafting;

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
 * Codecs for {@link KitchenMachineRecipe}. One serializer per {@link KitchenMachine}: the machine
 * is baked into the codec rather than written into the JSON, so a recipe file cannot claim to be
 * for a machine other than the folder it is registered under.
 */
public final class KitchenMachineRecipeSerializer {

    private KitchenMachineRecipeSerializer() {
    }

    public static RecipeSerializer<KitchenMachineRecipe> create(KitchenMachine machine) {
        return new RecipeSerializer<>(codec(machine), streamCodec(machine));
    }

    private static MapCodec<KitchenMachineRecipe> codec(KitchenMachine machine) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(KitchenMachineRecipe::group),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(KitchenMachineRecipe::getIngredient),
                ItemStackTemplate.CODEC.fieldOf("result").forGetter(KitchenMachineRecipe::getResultTemplate),
                Codec.INT.optionalFieldOf("processtime", machine.getDefaultProcessTime()).forGetter(KitchenMachineRecipe::getProcessTime)
        ).apply(instance, (group, ingredient, result, processTime) -> new KitchenMachineRecipe(machine, group, ingredient, result, processTime)));
    }

    private static StreamCodec<RegistryFriendlyByteBuf, KitchenMachineRecipe> streamCodec(KitchenMachine machine) {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, KitchenMachineRecipe::group,
                Ingredient.CONTENTS_STREAM_CODEC, KitchenMachineRecipe::getIngredient,
                ItemStackTemplate.STREAM_CODEC, KitchenMachineRecipe::getResultTemplate,
                ByteBufCodecs.VAR_INT, KitchenMachineRecipe::getProcessTime,
                (group, ingredient, result, processTime) -> new KitchenMachineRecipe(machine, group, ingredient, result, processTime));
    }
}
