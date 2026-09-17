package com.grim3212.assorted.cuisine.api;

import com.grim3212.assorted.cuisine.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

/** The damage this mod can do to you, which is one spiked orange soda. */
public class CuisineDamageTypes {

    public static final ResourceKey<DamageType> SPIKED_SODA = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "spiked_soda"));

    /**
     * A damage source for one of this mod's damage types, resolved against the level's damage type
     * registry ({@code DamageSources#source} is private).
     */
    public static DamageSource source(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }
}
