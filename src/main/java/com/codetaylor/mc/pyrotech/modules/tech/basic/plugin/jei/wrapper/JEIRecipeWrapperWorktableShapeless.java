package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import mezz.jei.recipes.BrokenCraftingRecipeException;
import mezz.jei.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIRecipeWrapperWorktableShapeless<T extends IRecipe>
    extends ShapelessRecipeWrapper<T> {

  private final IJeiHelpers jeiHelpers;
  private final List<ItemStack> toolList;
  private final int toolDamage;

  public JEIRecipeWrapperWorktableShapeless(IJeiHelpers jeiHelpers, T recipe, @Nullable Ingredient tool, int toolDamage) {

    super(jeiHelpers, recipe);
    this.jeiHelpers = jeiHelpers;
    this.toolDamage = toolDamage;
    this.toolList = new ArrayList<>();

    if (tool != null) {
      ItemStack[] matchingStacks = tool.getMatchingStacks();
      this.toolList.addAll(Arrays.asList(matchingStacks));
    }
  }

  public List<ItemStack> getToolList() {

    return this.toolList;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {

    ItemStack recipeOutput = this.recipe.getRecipeOutput();
    IStackHelper stackHelper = this.jeiHelpers.getStackHelper();

    try {
      List<List<ItemStack>> inputLists = stackHelper.expandRecipeItemStackInputs(this.recipe.getIngredients());
      ingredients.setInputLists(VanillaTypes.ITEM, inputLists);
      ingredients.setOutput(VanillaTypes.ITEM, recipeOutput);

    } catch (RuntimeException e) {
      String info = ErrorUtil.getInfoFromBrokenCraftingRecipe(this.recipe, this.recipe.getIngredients(), recipeOutput);
      throw new BrokenCraftingRecipeException(info, e);
    }
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    if (this.toolList != null) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(0, 0, 1000);
      minecraft.fontRenderer.drawStringWithShadow("-" + this.toolDamage, 65, 12, Color.WHITE.getRGB());
      GlStateManager.popMatrix();
    }
  }
}
