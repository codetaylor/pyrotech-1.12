package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.spi;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperSawmill;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class JEIRecipeCategorySawmillBase
    implements IRecipeCategory<JEIRecipeWrapperSawmill> {

  private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(ModuleTechMachine.MOD_ID, "textures/gui/jei11.png");

  private final IDrawableAnimated arrow;
  private final IDrawable background;
  private final String title;

  public JEIRecipeCategorySawmillBase(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = this.getBackgroundResourceLocation();

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 101, 0, 24, 17);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;

    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 101, 38);

    this.title = Translator.translateToLocal(this.getTitleKey());
  }

  protected abstract String getTitleKey();

  protected ResourceLocation getBackgroundResourceLocation() {

    return RESOURCE_LOCATION;
  }

  @Nonnull
  @Override
  public String getTitle() {

    return this.title;
  }

  @Nonnull
  @Override
  public String getModName() {

    return ModuleTechMachine.MOD_ID;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Override
  public void drawExtras(Minecraft minecraft) {

    this.arrow.draw(minecraft, 24, 16);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperSawmill recipeWrapper, IIngredients ingredients) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 0);
    itemStacks.init(1, true, 0, 19);
    itemStacks.init(2, false, 60, 16);
    itemStacks.init(3, false, 83, 20);

    itemStacks.set(ingredients);
  }
}
