package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper.JEIRecipeWrapperChoppingBlock;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryChoppingBlock
    implements IRecipeCategory<JEIRecipeWrapperChoppingBlock> {

  public static final String UID = ModuleTechBasic.MOD_ID + ".chopping";

  private final IDrawableAnimated arrow;
  private final IDrawable background;

  private final String title;

  public JEIRecipeCategoryChoppingBlock(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModulePyrotech.MOD_ID, "textures/gui/jei3.png");

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 82, 0, 24, 17);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;

    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 82, 40);

    this.title = Translator.translateToLocal("gui." + ModulePyrotech.MOD_ID + ".jei.category.chopping");
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

    return ModulePyrotech.MOD_ID;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Override
  public void drawExtras(Minecraft minecraft) {

    this.arrow.draw(minecraft, 24, 18);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull JEIRecipeWrapperChoppingBlock recipeWrapper, @Nonnull IIngredients ingredients) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 17);
    itemStacks.init(1, false, 60, 18);

    itemStacks.set(ingredients);
  }
}
