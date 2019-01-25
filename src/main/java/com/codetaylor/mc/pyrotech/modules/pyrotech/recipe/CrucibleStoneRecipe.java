package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class CrucibleStoneRecipe
    extends StoneMachineRecipeItemInFluidOutBase<CrucibleStoneRecipe> {

  @Nullable
  public static CrucibleStoneRecipe getRecipe(ItemStack input) {

    for (CrucibleStoneRecipe recipe : ModPyrotechRegistries.CRUCIBLE_STONE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(FluidStack output) {

    return RecipeHelper.removeRecipesByOutput(ModPyrotechRegistries.CRUCIBLE_STONE_RECIPE, output);
  }

  public CrucibleStoneRecipe(
      FluidStack output,
      Ingredient input,
      int burnTimeTicks
  ) {

    super(input, output, burnTimeTicks);
  }
}
