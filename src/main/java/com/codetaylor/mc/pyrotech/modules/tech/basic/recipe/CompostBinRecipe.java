package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.CompostBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class CompostBinRecipe
    extends CompostBinRecipeBase<CompostBinRecipe> {

  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input) {

    for (CompostBinRecipe recipe : ModuleTechBasic.Registries.COMPOST_BIN_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input, ItemStack output) {

    for (CompostBinRecipe recipe : ModuleTechBasic.Registries.COMPOST_BIN_RECIPE) {

      if (recipe.matches(input, output)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.COMPOST_BIN_RECIPE, output);
  }

  public CompostBinRecipe(ItemStack output, ItemStack input) {

    super(output, input);
  }

  public CompostBinRecipe(ItemStack output, ItemStack input, int compostValue) {

    super(output, input, compostValue);
  }
}
