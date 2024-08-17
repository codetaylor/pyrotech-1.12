package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class BloomeryRecipe
    extends BloomeryRecipeBase<BloomeryRecipe> {

  @Nullable
  public static BloomeryRecipe getRecipe(ItemStack input) {

    for (BloomeryRecipe recipe : ModuleTechBloomery.Registries.BLOOMERY_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBloomery.Registries.BLOOMERY_RECIPE, output);
  }

  public BloomeryRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float experience,
      float failureChance,
      int bloomYieldMin,
      int bloomYieldMax,
      int slagCount,
      ItemStack slagItem,
      FailureItem[] failureItems,
      AnvilRecipe.EnumTier[] anvilTiers,
      @Nullable String langKey
  ) {

    super(input, output, burnTimeTicks, experience, failureChance, bloomYieldMin, bloomYieldMax, slagCount, failureItems, slagItem, anvilTiers, langKey);
  }

}
