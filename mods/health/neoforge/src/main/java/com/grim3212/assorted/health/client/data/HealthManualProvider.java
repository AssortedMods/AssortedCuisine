package com.grim3212.assorted.health.client.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.health.Family;
import com.grim3212.assorted.health.common.item.HealthItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapters of the Assorted Cuisine section, which every part shares; the explicit chapter orders keep
 * the section's order whichever parts are installed.
 */
public class HealthManualProvider extends LibManualProvider {

    public HealthManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder sugar = this.chapter("sugar", 6);
        sugar.recipes("sweets", HealthItems.POWERED_SUGAR.get(), HealthItems.SWEETS.get(), HealthItems.POWERED_SWEETS.get()).every(60)
                .opens(HealthItems.POWERED_SUGAR.get(), HealthItems.SWEETS.get(), HealthItems.POWERED_SWEETS.get());

        ChapterBuilder health = this.chapter("health", 7);
        health.recipes("packs", HealthItems.BANDAGE.get(), HealthItems.HEALTHPACK.get(), HealthItems.HEALTHPACK_SUPER.get()).every(60)
                .opens(HealthItems.BANDAGE.get(), HealthItems.HEALTHPACK.get(), HealthItems.HEALTHPACK_SUPER.get());
    }
}
