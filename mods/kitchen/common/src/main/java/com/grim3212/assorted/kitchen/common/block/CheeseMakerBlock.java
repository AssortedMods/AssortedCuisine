package com.grim3212.assorted.kitchen.common.block;

import com.grim3212.assorted.kitchen.api.crafting.KitchenMachine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;

/**
 * Milk in, a block of cheese out. Right click with a milk bucket to start it, wait for it to
 * curdle, then right click again to collect.
 *
 * <p>What it accepts is an {@code assortedkitchen:cheese_making} recipe, not a hardcoded milk
 * bucket, so a pack can teach it goat cheese.
 */
public class CheeseMakerBlock extends KitchenMachineBlock {

    public CheeseMakerBlock(Properties props) {
        super(KitchenMachine.CHEESE_MAKER, props);
    }

    /** Milk separating: a few splashes over the open top. */
    @Override
    protected @Nullable ParticleOptions workingParticle() {
        return ParticleTypes.SPLASH;
    }
}
