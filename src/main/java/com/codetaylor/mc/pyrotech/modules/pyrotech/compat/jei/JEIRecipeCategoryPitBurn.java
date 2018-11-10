package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryPitBurn
    implements IRecipeCategory<JEIRecipeWrapperPitBurn> {

  private final IDrawableAnimated animatedFlame;
  private final IDrawableAnimated arrow;
  private final IDrawable background;

  private final String title;

  public JEIRecipeCategoryPitBurn(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModulePyrotech.MOD_ID, "textures/gui/jei.png");

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 82, 14, 24, 17);
    IDrawableStatic staticFlame = guiHelper.createDrawable(resourceLocation, 82, 0, 14, 14);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;
    IDrawableAnimated.StartDirection top = IDrawableAnimated.StartDirection.TOP;

    this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, top, true);
    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 82, 54);

    this.title = Translator.translateToLocal("gui." + ModulePyrotech.MOD_ID + ".jei.category.burn.pit");
  }

  @Nonnull
  @Override
  public String getUid() {

    return JEIRecipeCategoryUid.PIT_BURN;
  }

  @Nonnull
  @Override
  public String getTitle() {

    return this.title;
  }

  @Nonnull
  @Override
  public String getModName() {

    return ModulePyrotech.MOD_ID;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Override
  public void drawExtras(Minecraft minecraft) {

    this.animatedFlame.draw(minecraft, 1, 10);
    this.arrow.draw(minecraft, 24, 18);
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperPitBurn recipeWrapper, IIngredients ingredients) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 25);
    itemStacks.init(1, false, 60, 18);

    itemStacks.set(ingredients);

    IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
    int capacity = ModulePyrotechConfig.REFRACTORY.ACTIVE_PILE_MAX_FLUID_CAPACITY;
    fluidStacks.init(2, false, 56, 42, 26, 8, capacity, false, null);

    if (recipeWrapper.getFluidStack() != null) {
      fluidStacks.set(2, recipeWrapper.getFluidStack());
    }
  }
}
