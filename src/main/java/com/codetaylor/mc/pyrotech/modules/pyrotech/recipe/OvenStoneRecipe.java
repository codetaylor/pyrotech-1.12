package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class OvenStoneRecipe
    extends StoneMachineRecipeBase<OvenStoneRecipe> {

  private static final Map<String, OvenStoneRecipe> RECIPES = new HashMap<>();

  private OvenStoneRecipe(Ingredient input, ItemStack output, int timeTicks) {

    super(input, output, timeTicks);
  }

  public static OvenStoneRecipe getRecipe(ItemStack itemStack) {

    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(itemStack);

    if (result.isEmpty()
        || !(result.getItem() instanceof ItemFood)) {
      return null;
    }

    String key = itemStack.toString();

    OvenStoneRecipe recipe = RECIPES.get(key);

    if (recipe == null) {
      recipe = new OvenStoneRecipe(Ingredient.fromStacks(itemStack), result, ModulePyrotechConfig.STONE_OVEN.COOK_TIME_TICKS);
      RECIPES.put(key, recipe);
    }

    return recipe;
  }
}
