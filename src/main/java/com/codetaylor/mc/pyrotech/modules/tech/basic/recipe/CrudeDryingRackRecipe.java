package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.spi.DryingRackRecipeBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class CrudeDryingRackRecipe
    extends DryingRackRecipeBase<CrudeDryingRackRecipe> {

  @Nullable
  public static CrudeDryingRackRecipe getRecipe(ItemStack input) {

    for (CrudeDryingRackRecipe recipe : ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE, output);
  }

  public CrudeDryingRackRecipe(
      ItemStack output,
      Ingredient input,
      int dryTimeTicks
  ) {

    super(input, output, dryTimeTicks);
  }

  @Override
  public int getTimeTicks() {

    return (int) Math.max(1, super.getTimeTicks() * ModuleTechBasicConfig.CRUDE_DRYING_RACK.BASE_RECIPE_DURATION_MODIFIER);
  }
}
