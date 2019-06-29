package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.List;

public class JEIRecipeWrapperWorktableShaped
    extends JEIRecipeWrapperWorktableShapeless<IShapedRecipe>
    implements IShapedCraftingRecipeWrapper {

  public JEIRecipeWrapperWorktableShaped(IJeiHelpers jeiHelpers, IShapedRecipe recipe, List<Item> tool, int toolDamage) {

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
