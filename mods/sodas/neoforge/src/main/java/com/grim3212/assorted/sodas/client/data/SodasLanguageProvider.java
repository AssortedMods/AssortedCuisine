package com.grim3212.assorted.sodas.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.sodas.Constants;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. An item whose name is its id in title case needs no line here (see
 * {@link LibLanguageProvider}); the manual's keys are the Assorted Cuisine section's, which every part shares.
 */
public class SodasLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public SodasLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedcuisine", "Assorted Cuisine");

        // Names that are not just the id in title case.
        this.add("item.assortedsodas.soda_bottle", "Soda Bottle");
        this.add("item.assortedsodas.soda_co2", "CO2 Canister");
        this.add("item.assortedsodas.soda_carbonated_water", "Carbonated Water");
        this.add("item.assortedsodas.soda_apple", "Apple Soda");
        this.add("item.assortedsodas.soda_golden_apple", "Golden Apple Soda");
        this.add("item.assortedsodas.soda_diamond", "Diamond Soda");
        this.add("item.assortedsodas.soda_cocoa", "Cocoa Soda");
        this.add("item.assortedsodas.soda_orange", "Orange Soda");
        this.add("item.assortedsodas.soda_cream_orange", "Orange Cream Soda");
        this.add("item.assortedsodas.soda_spiked_orange", "Spiked Orange Soda");
        this.add("item.assortedsodas.soda_root_beer", "Root Beer");
        this.add("item.assortedsodas.soda_mushroom", "Mushroom Soda");
        this.add("item.assortedsodas.soda_slurm", "Slurm");

        // The soda tooltips, so thirteen near-identical bottles can be told apart.
        this.add("tooltip.assortedsodas.restores", "Restores %s hearts");
        this.add("tooltip.assortedsodas.hurts", "Costs %s hearts");

        this.add("death.attack.assortedsodas.spiked_soda", "%1$s drank a spiked orange soda");
        this.add("death.attack.assortedsodas.spiked_soda.player", "%1$s drank a spiked orange soda given to them by %2$s");

        this.add("tag.item.assortedsodas.sodas", "Sodas");

        this.addManual();
        this.addAdvancements();
    }

    /** This part's chapter of the Assorted Cuisine section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedcuisine.title", "Assorted Cuisine");
        this.add("manual.assortedcuisine.description",
                "Cocoa, chocolate, dairy and everything else this mod gives you to cook, bottle and eat.");

        this.add("manual.assortedcuisine.chapter.soda", "Soda");

        this.add("manual.assortedcuisine.chapter.soda.carbonated.title", "Carbonated Water");
        this.add("manual.assortedcuisine.chapter.soda.carbonated",
                "Every soda starts the same way: a soda bottle, some carbon dioxide, and water to carbonate. "
                        + "Get that far and the flavour is the easy part.");

        this.add("manual.assortedcuisine.chapter.soda.types.title", "Flavours");
        this.add("manual.assortedcuisine.chapter.soda.types",
                "Ten flavours, and they do not heal the same. Slurm and root beer are light, apple and the two "
                        + "oranges are ordinary, and cocoa, golden apple and diamond are worth saving." + BREAK
                        + "Spiked orange is the exception. It hurts whoever drinks it, which is the whole point "
                        + "of handing one to somebody else.");
    }

    /** The Assorted Cuisine root, which every part with advancements writes the same, and this part's own. */
    private void addAdvancements() {
        this.add("advancements.assortedcuisine.root.title", "Assorted Cuisine");
        this.add("advancements.assortedcuisine.root.description", "Cook something worth eating");

        this.advancement("soda", "Fizzy Lifting", "Bottle your first soda");
        this.advancement("every_soda", "Taste Test", "Collect all ten drinkable flavours");
        this.advancement("spiked", "Hold My Drink", "Find out what is in a spiked orange soda");
    }

    private void advancement(String name, String title, String description) {
        this.add("advancements." + Constants.MOD_ID + "." + name + ".title", title);
        this.add("advancements." + Constants.MOD_ID + "." + name + ".description", description);
    }
}
