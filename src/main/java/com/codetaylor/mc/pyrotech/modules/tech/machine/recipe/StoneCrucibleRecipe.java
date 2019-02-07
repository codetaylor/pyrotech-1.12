package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInFluidOutBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class StoneCrucibleRecipe
    extends StoneMachineRecipeItemInFluidOutBase<StoneCrucibleRecipe> {

  @Nullable
  public static StoneCrucibleRecipe getRecipe(ItemStack input) {

    for (StoneCrucibleRecipe recipe : ModuleTechMachine.Registries.STONE_CRUCIBLE_RECIPES) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(FluidStack output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.STONE_CRUCIBLE_RECIPES, output);
  }

  public StoneCrucibleRecipe(
      FluidStack output,
      Ingredient input,
      int burnTimeTicks
  ) {

    super(input, output, burnTimeTicks);
  }
}
