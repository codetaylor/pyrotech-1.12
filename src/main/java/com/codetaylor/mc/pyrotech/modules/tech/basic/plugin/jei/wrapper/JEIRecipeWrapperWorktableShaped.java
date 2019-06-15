package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class JEIRecipeWrapperWorktableShaped
    extends JEIRecipeWrapperWorktableShapeless<IShapedRecipe>
    implements IShapedCraftingRecipeWrapper {

  public JEIRecipeWrapperWorktableShaped(IJeiHelpers jeiHelpers, IShapedRecipe recipe, Ingredient tool, int toolDamage) {

    super(jeiHelpers, recipe, tool, toolDamage);
  }

  @Override
  public int getWidth() {

    return this.recipe.getRecipeWidth();
  }

  @Override
  public int getHeight() {

    return this.recipe.getRecipeHeight();
  }
}
