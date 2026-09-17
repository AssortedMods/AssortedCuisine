package com.grim3212.assorted.cuisine.compat.jei;

import com.grim3212.assorted.cuisine.api.crafting.CuisineMachine;
import com.grim3212.assorted.cuisine.api.crafting.CuisineMachineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * One JEI page for one machine: what goes in, an arrow that runs for as long as the recipe takes,
 * and what comes out. The three machines have no container screen between them, so the category
 * paints itself out of JEI's own slot and arrow drawables rather than a GUI texture.
 */
public class CuisineMachineRecipeCategory implements IRecipeCategory<CuisineMachineRecipe> {

    private static final int WIDTH = 92;
    private static final int HEIGHT = 34;
    private static final int SLOT_Y = 5;

    private final IRecipeType<CuisineMachineRecipe> type;
    private final CuisineMachine machine;
    private final IGuiHelper guiHelper;
    private final IDrawableStatic slot;
    private final IDrawable icon;
    private final Component title;

    /** One arrow per distinct process time, so a pack's slower recipe animates at its own speed. */
    private final Map<Integer, IDrawableAnimated> arrows = new HashMap<>();

    public CuisineMachineRecipeCategory(IGuiHelper guiHelper, IRecipeType<CuisineMachineRecipe> type, CuisineMachine machine, Block catalyst) {
        this.guiHelper = guiHelper;
        this.type = type;
        this.machine = machine;
        this.slot = guiHelper.getSlotDrawable();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(catalyst));
        this.title = catalyst.getName();
    }

    @Override
    public IRecipeType<CuisineMachineRecipe> getRecipeType() {
        return this.type;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder layout, CuisineMachineRecipe recipe, IFocusGroup focuses) {
        layout.addSlot(RecipeIngredientRole.INPUT, 1, SLOT_Y + 1)
                .setBackground(this.slot, -1, -1)
                .add(recipe.getIngredient());

        layout.addSlot(RecipeIngredientRole.OUTPUT, 71, SLOT_Y + 1)
                .setBackground(this.slot, -1, -1)
                .add(recipe.getResultTemplate());
    }

    @Override
    public void draw(CuisineMachineRecipe recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.arrow(recipe.getProcessTime()).draw(graphics, 26, SLOT_Y + 4);

        Font font = Minecraft.getInstance().font;
        Component time = Component.translatable("gui.jei.category.smelting.time.seconds", recipe.getProcessTime() / 20);
        graphics.text(font, time, (WIDTH - font.width(time)) / 2, HEIGHT - 9, 0xFF808080, false);
    }

    private IDrawableAnimated arrow(int processTime) {
        return this.arrows.computeIfAbsent(processTime <= 0 ? this.machine.getDefaultProcessTime() : processTime,
                this.guiHelper::createAnimatedRecipeArrow);
    }
}
