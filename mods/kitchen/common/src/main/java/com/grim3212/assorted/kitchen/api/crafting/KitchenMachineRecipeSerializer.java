package com.grim3212.assorted.kitchen.api.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Codecs for {@link KitchenMachineRecipe}. One serializer per {@link KitchenMachine}: the machine
 * is baked into the codec rather than written into the JSON, so a recipe file cannot claim to be
 * for a machine other than the folder it is registered under.
 */
public final class KitchenMachineRecipeSerializer implements RecipeSerializer<KitchenMachineRecipe> {

    private final MapCodec<KitchenMachineRecipe> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, KitchenMachineRecipe> streamCodec;

    private KitchenMachineRecipeSerializer(KitchenMachine machine) {
        this.codec = codec(machine);
        this.streamCodec = streamCodec(machine);
    }

    public static RecipeSerializer<KitchenMachineRecipe> create(KitchenMachine machine) {
        return new KitchenMachineRecipeSerializer(machine);
    }

    @Override
    public MapCodec<KitchenMachineRecipe> codec() {
        return this.codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, KitchenMachineRecipe> streamCodec() {
        return this.streamCodec;
    }

    private static MapCodec<KitchenMachineRecipe> codec(KitchenMachine machine) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(KitchenMachineRecipe::getGroup),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(KitchenMachineRecipe::getIngredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(KitchenMachineRecipe::getResultItem),
                Codec.INT.optionalFieldOf("processtime", machine.getDefaultProcessTime()).forGetter(KitchenMachineRecipe::getProcessTime)
        ).apply(instance, (group, ingredient, result, processTime) -> new KitchenMachineRecipe(machine, group, ingredient, result, processTime)));
    }

    /** Every recipe is sent to every client in 1.21.1, so this carries the whole recipe. */
    private static StreamCodec<RegistryFriendlyByteBuf, KitchenMachineRecipe> streamCodec(KitchenMachine machine) {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, KitchenMachineRecipe::getGroup,
                Ingredient.CONTENTS_STREAM_CODEC, KitchenMachineRecipe::getIngredient,
                ItemStack.STREAM_CODEC, KitchenMachineRecipe::getResultItem,
                ByteBufCodecs.VAR_INT, KitchenMachineRecipe::getProcessTime,
                (group, ingredient, result, processTime) -> new KitchenMachineRecipe(machine, group, ingredient, result, processTime));
    }
}
