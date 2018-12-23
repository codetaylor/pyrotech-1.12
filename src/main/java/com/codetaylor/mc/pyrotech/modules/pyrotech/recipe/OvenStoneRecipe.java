package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class OvenStoneRecipe
    extends StoneMachineRecipeBase<OvenStoneRecipe> {

  private static final Map<String, OvenStoneRecipe> RECIPES = new HashMap<>();

  public OvenStoneRecipe(Ingredient input, ItemStack output, int timeTicks) {

    super(input, output, timeTicks);
  }

  public static OvenStoneRecipe getRecipe(ItemStack itemStack) {

    ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(itemStack);

    if (smeltingResult.isEmpty()) {
      return null;
    }

    String key = itemStack.toString();

    OvenStoneRecipe recipe = RECIPES.get(key);

    if (recipe == null) {
      recipe = new OvenStoneRecipe(Ingredient.fromStacks(itemStack), smeltingResult, 100);
      RECIPES.put(key, recipe);
    }

    return recipe;
  }
}
