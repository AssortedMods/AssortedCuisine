package com.grim3212.assorted.health.gametest;

import net.minecraft.core.BlockPos;

/**
 * Helpers and constants shared by Assorted Health's gametest classes, which import them statically,
 * alongside AssortedLib's {@code TestSupport}.
 */
final class HealthTestSupport {

    private HealthTestSupport() {
    }

    /** Middle of the 9x9x9 box, one block above its floor - room on every side for a drop. */
    static final BlockPos CENTRE = new BlockPos(4, 1, 4);
}
