package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class WitherForgeRecipe
    extends BloomeryRecipeBase<WitherForgeRecipe> {

  @Nullable
  public static WitherForgeRecipe getRecipe(ItemStack input) {

    for (WitherForgeRecipe recipe : ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE, output);
  }

  /* package */ WitherForgeRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      int bloomYieldMin,
      int bloomYieldMax,
      int slagCount,
      ItemStack slagItem,
      FailureItem[] failureItems,
      @Nullable String langKey
  ) {

    super(input, output, burnTimeTicks, failureChance, bloomYieldMin, bloomYieldMax, slagCount, failureItems, slagItem, langKey);
  }

}
