package com.grim3212.assorted.health.client.data;

import com.grim3212.assorted.health.Constants;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. An item whose name is its id in title case needs no line here (see
 * {@link LibLanguageProvider}); the manual's keys are the Assorted Cuisine section's, which every part shares.
 */
public class HealthLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public HealthLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedcuisine", "Assorted Cuisine");

        // Names that are not just the id in title case.
        this.add("item.assortedhealth.powered_sugar", "Powered Sugar");
        this.add("item.assortedhealth.powered_sweets", "Powered Sweets");
        this.add("item.assortedhealth.sweets", "Sugar Sweets");
        this.add("item.assortedhealth.healthpack", "Health Pack");
        this.add("item.assortedhealth.healthpack_super", "Super Health Pack");

        // The healing tooltip, so each pack says how much it heals.
        this.add("tooltip.assortedhealth.restores", "Restores %s hearts");

        this.addManual();
        this.addAdvancements();
    }

    /** This part's chapters of the Assorted Cuisine section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedcuisine.title", "Assorted Cuisine");
        this.add("manual.assortedcuisine.description",
                "Cocoa, chocolate, dairy and everything else this mod gives you to cook, bottle and eat.");

        this.add("manual.assortedcuisine.chapter.sugar", "Sweets");

        this.add("manual.assortedcuisine.chapter.sugar.sweets.title", "Sweets");
        this.add("manual.assortedcuisine.chapter.sugar.sweets",
                "Sugar and paper make sweets, and powered sugar and paper make powered sweets, the stronger "
                        + "kind. Neither is a meal, but both stack deep and eat quickly." + BREAK
                        + "Powered sugar is sugar with a little redstone, and it is also what the super health pack "
                        + "is built on.");

        this.add("manual.assortedcuisine.chapter.health", "Healing");

        this.add("manual.assortedcuisine.chapter.health.packs.title", "Bandages and Packs");
        this.add("manual.assortedcuisine.chapter.health.packs",
                "Food heals slowly and only while you are fed. These three heal directly, which is what you "
                        + "want halfway through a fight - but each takes a moment to apply, and being hit "
                        + "interrupts it." + BREAK
                        + "The bandage is the cheap one, the health pack the middle, and the super pack needs "
                        + "powered sugar from the previous chapter.");
    }

    /** The Assorted Cuisine root, which every part with advancements writes the same, and this part's own. */
    private void addAdvancements() {
        this.add("advancements.assortedcuisine.root.title", "Assorted Cuisine");
        this.add("advancements.assortedcuisine.root.description", "Cook something worth eating");

        this.advancement("field_medic", "Field Medic", "Build a super health pack");
    }

    private void advancement(String name, String title, String description) {
        this.add("advancements." + Constants.MOD_ID + "." + name + ".title", title);
        this.add("advancements." + Constants.MOD_ID + "." + name + ".description", description);
    }
}
