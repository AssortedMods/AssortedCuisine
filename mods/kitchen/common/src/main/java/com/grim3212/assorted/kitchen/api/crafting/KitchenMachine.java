package com.grim3212.assorted.kitchen.api.crafting;

import com.grim3212.assorted.kitchen.Constants;
import net.minecraft.resources.Identifier;

/**
 * The three blocks that turn one item into another over time. Each owns a recipe type, so a pack
 * can add goat cheese or a new chocolate without touching the block.
 *
 * <p>All three share one recipe shape and one block entity; what differs is the registry name, how
 * long the default recipe takes, and whether the block speeds itself up (see
 * {@code KitchenMachineBlock#speedMultiplier}).
 */
public enum KitchenMachine {

    CHEESE_MAKER("cheese_making", 600),
    /** The churn also takes progress from being turned by hand; see {@code ButterChurnBlock}. */
    BUTTER_CHURN("churning", 400),
    CHOCOLATE_MOLD("chocolate_molding", 400);

    private final String name;
    private final int defaultProcessTime;

    KitchenMachine(String name, int defaultProcessTime) {
        this.name = name;
        this.defaultProcessTime = defaultProcessTime;
    }

    public String getName() {
        return this.name;
    }

    /** Ticks a recipe takes when it does not say, which is every recipe this mod ships. */
    public int getDefaultProcessTime() {
        return this.defaultProcessTime;
    }

    public Identifier id() {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, this.name);
    }
}
