package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.spi;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperOven;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class JEIRecipeCategoryOvenBase
    implements IRecipeCategory<JEIRecipeWrapperOven> {

  private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(ModuleTechMachine.MOD_ID, "textures/gui/jei2.png");

  private final IDrawableAnimated animatedFlame;
  private final IDrawableAnimated arrow;
  private final IDrawable background;
  private final String title;

  public JEIRecipeCategoryOvenBase(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = this.getBackgroundResourceLocation();

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 82, 14, 24, 17);
    IDrawableStatic staticFlame = guiHelper.createDrawable(resourceLocation, 82, 0, 14, 14);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;
    IDrawableAnimated.StartDirection top = IDrawableAnimated.StartDirection.TOP;

    this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, top, true);
    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 82, 33);

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

    this.animatedFlame.draw(minecraft, 1, 19);
    this.arrow.draw(minecraft, 24, 10);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperOven recipeWrapper, IIngredients ingredients) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 0);
    itemStacks.init(1, false, 60, 10);

    itemStacks.set(ingredients);
  }
}
