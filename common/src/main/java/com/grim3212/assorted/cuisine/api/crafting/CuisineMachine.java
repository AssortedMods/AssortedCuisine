package com.grim3212.assorted.cuisine.api.crafting;

import com.grim3212.assorted.cuisine.Constants;
import net.minecraft.resources.Identifier;

/**
 * The three blocks that turn one item into another over time. Each owns a recipe type, so a pack
 * can add goat cheese or a new chocolate without touching the block.
 *
 * <p>All three share one recipe shape and one block entity; what differs is the registry name, how
 * long the default recipe takes, and whether the block speeds itself up (see
 * {@code CuisineMachineBlock#speedMultiplier}).
 */
public enum CuisineMachine {

    CHEESE_MAKER("cheese_making", 600),
    /** The churn also takes progress from being turned by hand; see {@code ButterChurnBlock}. */
    BUTTER_CHURN("churning", 400),
    CHOCOLATE_MOULD("chocolate_moulding", 400);

    private final String name;
    private final int defaultProcessTime;

    CuisineMachine(String name, int defaultProcessTime) {
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
