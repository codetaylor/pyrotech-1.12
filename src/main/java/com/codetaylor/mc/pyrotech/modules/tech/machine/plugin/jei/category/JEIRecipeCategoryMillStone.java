package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category;

import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperMillStone;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class JEIRecipeCategoryMillStone
    implements IRecipeCategory<JEIRecipeWrapperMillStone> {

  public static final String UID = ModuleTechMachine.MOD_ID + ".stone.mill";

  private final IDrawableAnimated arrow;
  private final IDrawable background;

  private final String title;

  public JEIRecipeCategoryMillStone(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechMachine.MOD_ID, "textures/gui/jei4.png");

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 82, 0, 24, 17);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;

    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 82, 38);

    this.title = Translator.translateToLocal("gui." + ModuleTechMachine.MOD_ID + ".jei.category.mill.stone");
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
  public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperMillStone recipeWrapper, IIngredients ingredients) {

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 0);
    itemStacks.init(1, true, 0, 19);
    itemStacks.init(2, false, 60, 16);

    itemStacks.set(ingredients);
  }
}
