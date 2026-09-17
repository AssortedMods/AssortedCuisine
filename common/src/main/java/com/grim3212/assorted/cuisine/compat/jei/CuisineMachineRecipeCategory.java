package com.grim3212.assorted.cuisine.compat.jei;

import com.grim3212.assorted.cuisine.Constants;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * One JEI page for one machine, drawn on the same strip the instruction manual uses: the block that
 * does the work, what goes in, an arrow that fills over the time the recipe takes, and what comes
 * out. Sharing the texture is the point - a player who has seen the page in the book should
 * recognise it here.
 */
public class CuisineMachineRecipeCategory implements IRecipeCategory<CuisineMachineRecipe> {

    /**
     * The strip every machine is drawn on, shared with the instruction manual's recipe layouts. The
     * three processes look the same - a block, an input, an arrow, a result - so one file serves
     * them all, including the filled arrow parked below the strip.
     */
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/container/cuisine_machine.png");

    /** The strip, and the slot and arrow positions baked into it. */
    private static final int WIDTH = 98;
    private static final int STRIP_HEIGHT = 26;
    private static final int STATION_X = 5;
    private static final int INPUT_X = 27;
    private static final int OUTPUT_X = 77;
    private static final int SLOT_Y = 5;
    private static final int ARROW_X = 48;
    private static final int ARROW_Y = 5;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 16;

    /** Room under the strip for the time in seconds. */
    private static final int HEIGHT = STRIP_HEIGHT + 11;

    private final IRecipeType<CuisineMachineRecipe> type;
    private final CuisineMachine machine;
    private final ItemStack station;
    private final IGuiHelper guiHelper;
    private final IDrawableStatic background;
    private final IDrawable icon;
    private final Component title;

    /** One arrow per distinct process time, so a pack's slower recipe animates at its own speed. */
    private final Map<Integer, IDrawableAnimated> arrows = new HashMap<>();

    public CuisineMachineRecipeCategory(IGuiHelper guiHelper, IRecipeType<CuisineMachineRecipe> type, CuisineMachine machine, Block catalyst) {
        this.guiHelper = guiHelper;
        this.type = type;
        this.machine = machine;
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, WIDTH, STRIP_HEIGHT);
        this.station = new ItemStack(catalyst);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, this.station);
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

    /** No slot backgrounds: the strip already has the frames drawn in the right places. */
    @Override
    public void setRecipe(IRecipeLayoutBuilder layout, CuisineMachineRecipe recipe, IFocusGroup focuses) {
        // The block that does the work, in its own slot on the left. Saying so in the tooltip is
        // the whole point of it being here: the page is otherwise silent about where this happens.
        layout.addSlot(RecipeIngredientRole.CRAFTING_STATION, STATION_X, SLOT_Y)
                .add(this.station)
                .addRichTooltipCallback((slot, tooltip) ->
                        tooltip.add(Component.translatable("tooltip.assortedcuisine.made_in", this.station.getHoverName())
                                .withStyle(ChatFormatting.GRAY)));

        layout.addSlot(RecipeIngredientRole.INPUT, INPUT_X, SLOT_Y).add(recipe.getIngredient());
        layout.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, SLOT_Y).add(recipe.getResultTemplate());
    }

    @Override
    public void draw(CuisineMachineRecipe recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.background.draw(graphics, 0, 0);
        this.arrow(recipe.getProcessTime()).draw(graphics, ARROW_X, ARROW_Y);

        Font font = Minecraft.getInstance().font;
        Component time = Component.translatable("gui.jei.category.smelting.time.seconds", recipe.getProcessTime() / 20);
        graphics.text(font, time, (WIDTH - font.width(time)) / 2, STRIP_HEIGHT + 2, 0xFF808080, false);
    }

    /** The filled arrow is parked below the strip; it grows left to right over the recipe's time. */
    private IDrawableAnimated arrow(int processTime) {
        return this.arrows.computeIfAbsent(processTime <= 0 ? this.machine.getDefaultProcessTime() : processTime,
                ticks -> this.guiHelper.drawableBuilder(TEXTURE, 0, 32, ARROW_WIDTH, ARROW_HEIGHT)
                        .buildAnimated(ticks, IDrawableAnimated.StartDirection.LEFT, false));
    }
}
