package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.spi.DryingRackRecipeBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class DryingRackRecipe
    extends DryingRackRecipeBase<DryingRackRecipe> {

  @Nullable
  public static DryingRackRecipe getRecipe(ItemStack input) {

    for (DryingRackRecipe recipe : ModuleTechBasic.Registries.DRYING_RACK_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.DRYING_RACK_RECIPE, output);
  }

  public DryingRackRecipe(
      ItemStack output,
      Ingredient input,
      int dryTimeTicks
  ) {

    super(input, output, dryTimeTicks);
  }

}
