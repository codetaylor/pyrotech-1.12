package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category;

import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.PyrotechRecipeCategory;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper.JEIRecipeWrapperAnvil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryAnvilIronclad
    extends PyrotechRecipeCategory<JEIRecipeWrapperAnvil> {

  public static final String UID = ModuleTechBasic.MOD_ID + ".anvil.ironclad";

  private final IDrawableAnimated arrow;
  private final IDrawable background;

  private final String title;

  public JEIRecipeCategoryAnvilIronclad(IGuiHelper guiHelper) {

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, "textures/gui/jei3.png");

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation, 82, 0, 24, 17);

    IDrawableAnimated.StartDirection left = IDrawableAnimated.StartDirection.LEFT;

    this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, left, false);
    this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 82, 40);

    this.title = Translator.translateToLocal("gui." + ModuleTechBasic.MOD_ID + ".jei.category.anvil.ironclad");
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

    this.arrow.draw(minecraft, 24, 18);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull JEIRecipeWrapperAnvil recipeWrapper, @Nonnull IIngredients ingredients) {

    super.setRecipe(recipeLayout, recipeWrapper, ingredients);

    IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
    itemStacks.init(0, true, 0, 17);
    itemStacks.init(1, false, 60, 18);

    itemStacks.set(ingredients);
  }

  @Override
  protected int getOutputSlotIndex() {

    return 1;
  }
}
