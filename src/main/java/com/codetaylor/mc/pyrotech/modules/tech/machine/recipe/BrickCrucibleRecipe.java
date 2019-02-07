package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class BrickCrucibleRecipe
    extends MachineRecipeItemInFluidOutBase<BrickCrucibleRecipe> {

  @Nullable
  public static BrickCrucibleRecipe getRecipe(ItemStack input) {

    for (BrickCrucibleRecipe recipe : ModuleTechMachine.Registries.BRICK_CRUCIBLE_RECIPES) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(FluidStack output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.BRICK_CRUCIBLE_RECIPES, output);
  }

  public BrickCrucibleRecipe(
      FluidStack output,
      Ingredient input,
      int burnTimeTicks
  ) {

    super(input, output, burnTimeTicks);
  }
}
