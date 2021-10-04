package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.category;

import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.PyrotechRecipeCategory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.wrapper.JEIRecipeWrapperRefractoryBurn;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class JEIRecipeCategoryRefractoryBurn
    extends PyrotechRecipeCategory<JEIRecipeWrapperRefractoryBurn> {

  public static final String UID = ModuleTechRefractory.MOD_ID + ".refractory.burn";

  private final IDrawableAnimated animatedFlame;
  private final IDrawableAnimated arrow;
  private final IDrawable background;

  private final String title;

  public JEIRecipeCategoryRefractoryBurn(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechRefractory.MOD_ID, "textures/gui/jei5.png");

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 82 + 19, 14, 24, 17);
    IDrawableStatic staticFlame = guiHelper.createDrawable(resourceLocation, 82 + 19, 0, 14, 14);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;
    IDrawableAnimated.StartDirection top = IDrawableAnimated.StartDirection.TOP;

    this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, top, true);
    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 82 + 19, 54);

    this.title = Translator.translateToLocal("gui." + ModuleTechRefractory.MOD_ID + ".jei.category.burn.refractory");
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

    return ModuleTechRefractory.MOD_ID;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Override
  public void drawExtras(@Nonnull Minecraft minecraft) {

    this.animatedFlame.draw(minecraft, 1, 6);
    this.arrow.draw(minecraft, 24, 18);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(
      IRecipeLayout recipeLayout,
      JEIRecipeWrapperRefractoryBurn recipeWrapper,
      IIngredients ingredients
  ) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 22);
    itemStacks.init(1, false, 60, 4);
    itemStacks.init(2, false, 83, 8);

    itemStacks.set(ingredients);

    IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
    int capacity = ModuleTechRefractoryConfig.REFRACTORY.ACTIVE_PILE_MAX_FLUID_CAPACITY;
    fluidStacks.init(2, false, 56, 42, 26, 8, capacity, true, null);
    fluidStacks.init(2, false, 57, 42 - 14, 43, 11, capacity, true, null);

    if (recipeWrapper.getFluidStack() != null) {
      fluidStacks.set(2, recipeWrapper.getFluidStack());
    }
  }

  @Override
  protected int getOutputSlotIndex() {

    return 1;
  }
}
