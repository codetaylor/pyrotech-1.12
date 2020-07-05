package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper.JEIRecipeWrapperCompostBin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryCompostBin
    implements IRecipeCategory<JEIRecipeWrapperCompostBin> {

  public static final String UID = ModuleTechBasic.MOD_ID + ".compost.bin";

  private final IDrawableAnimated arrow;
  private final IDrawable background;
  private final String title;

  public JEIRecipeCategoryCompostBin(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, "textures/gui/jei12.png");
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 162, 165);

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 162, 0, 16, 23);
    IDrawableAnimated.StartDirection top = IDrawableAnimated.StartDirection.TOP;
    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, top, false);

    this.title = Translator.translateToLocal("gui." + ModuleTechBasic.MOD_ID + ".jei.category.compost.bin");
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
  public void drawExtras(Minecraft minecraft) {

    this.arrow.draw(minecraft, 73, 112);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull JEIRecipeWrapperCompostBin recipeWrapper, @Nonnull IIngredients ingredients) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

    // output
    itemStacks.init(0, false, 72, 143);

    // inputs
    int inputCount = recipeWrapper.getInputCount();

    for (int i = 0; i < inputCount; i++) {
      itemStacks.init(i + 1, true, 18 * (i % 9), 18 * (i / 9));
    }

    itemStacks.set(ingredients);
  }
}
