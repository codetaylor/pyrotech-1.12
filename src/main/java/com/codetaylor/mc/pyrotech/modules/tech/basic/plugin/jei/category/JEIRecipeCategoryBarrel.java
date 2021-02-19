package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category;

import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.PyrotechRecipeCategory;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper.JEIRecipeWrapperBarrel;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class JEIRecipeCategoryBarrel
    extends PyrotechRecipeCategory<JEIRecipeWrapperBarrel> {

  public static final String UID = ModuleTechBasic.MOD_ID + ".barrel";

  private final IDrawableAnimated arrow;
  private final IDrawable background;

  private final String title;

  public JEIRecipeCategoryBarrel(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, "textures/gui/jei13.png");
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 97, 51);

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 101, 0, 24, 17);
    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;
    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);

    this.title = Translator.translateToLocal("gui." + ModuleTechBasic.MOD_ID + ".jei.category.barrel");
  }

  @Nonnull
  @Override
  public String getUid() {

    return UID;
  }

  @Nonnull
  @Override
  public String getTitle() {

    return this.title;
  }

  @Nonnull
  @Override
  public String getModName() {

    return ModuleTechBasic.MOD_ID;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Override
  public void drawExtras(@Nonnull Minecraft minecraft) {

    this.arrow.draw(minecraft, 42, 19);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperBarrel recipeWrapper, IIngredients ingredients) {

    super.setRecipe(recipeLayout, recipeWrapper, ingredients);

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 0);
    itemStacks.init(1, true, 19, 0);
    itemStacks.init(2, true, 0, 19);
    itemStacks.init(3, true, 19, 19);
    itemStacks.set(ingredients);

    IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
    int capacity = recipeWrapper.getInputFluid().amount;
    fluidStacks.init(4, true, 1, 39, 35, 11, capacity, false, null);
    fluidStacks.init(5, false, 72, 1, 24, 49, capacity, false, null);
    fluidStacks.set(ingredients);
  }

  @Override
  protected int getOutputSlotIndex() {

    return 5;
  }
}
