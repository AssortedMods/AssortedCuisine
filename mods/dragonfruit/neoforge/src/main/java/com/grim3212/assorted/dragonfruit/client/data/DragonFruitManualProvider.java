package com.grim3212.assorted.dragonfruit.client.data;

import com.grim3212.assorted.dragonfruit.Constants;
import com.grim3212.assorted.dragonfruit.common.item.DragonFruitItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Cuisine section, which every part shares; the explicit chapter order keeps
 * the section's order whichever parts are installed.
 */
public class DragonFruitManualProvider extends LibManualProvider {

    public DragonFruitManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        ChapterBuilder dragonFruit = this.chapter("dragon_fruit", 3);
        dragonFruit.image("cactus", picture("dragon_fruit"), 69, 104).opens(DragonFruitItems.DRAGON_FRUIT.get());
        dragonFruit.recipes("cutting", DragonFruitItems.DRAGON_FRUIT.get());
    }

    /** The Grim Pack screenshots under {@code textures/gui/manual}, cropped and sized to leave room for the text. */
    private static Identifier picture(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/manual/" + name + ".png");
    }
}
