package com.grim3212.assorted.dragonfruit.client.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. The dragon fruit's name is its id in title case, so it needs no line here
 * (see {@link LibLanguageProvider}); the manual's keys are the Assorted Cuisine section's, which every part shares.
 */
public class DragonFruitLanguageProvider extends LibLanguageProvider {

    public DragonFruitLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedcuisine", "Assorted Cuisine");

        this.addManual();
        this.addAdvancements();
    }

    /** This part's chapter of the Assorted Cuisine section, and the section's own title, which every part writes the same. */
    private void addManual() {
        this.add("manual.assortedcuisine.title", "Assorted Cuisine");
        this.add("manual.assortedcuisine.description",
                "Cocoa, chocolate, dairy and everything else this mod gives you to cook, bottle and eat.");

        this.add("manual.assortedcuisine.chapter.dragon_fruit", "Dragon Fruit");

        this.add("manual.assortedcuisine.chapter.dragon_fruit.cactus.title", "Dragon Fruit");
        this.add("manual.assortedcuisine.chapter.dragon_fruit.cactus",
                "Cacti sometimes drop dragon fruit when they break. It is a small meal.");

        this.add("manual.assortedcuisine.chapter.dragon_fruit.cutting.title", "Cutting One Open");
        this.add("manual.assortedcuisine.chapter.dragon_fruit.cutting",
                "Waiting on the drop is slow, so cut one open instead. A cactus and a knife give a fruit every time.");
    }

    /** The Assorted Cuisine root, which every part with advancements writes the same, and this part's own. */
    private void addAdvancements() {
        this.add("advancements.assortedcuisine.root.title", "Assorted Cuisine");
        this.add("advancements.assortedcuisine.root.description", "Cook something worth eating");

        this.advancement("dragon_fruit", "Fruit of the Desert", "Take dragon fruit from a cactus");
    }

    private void advancement(String name, String title, String description) {
        this.add("advancements." + Constants.MOD_ID + "." + name + ".title", title);
        this.add("advancements." + Constants.MOD_ID + "." + name + ".description", description);
    }
}
