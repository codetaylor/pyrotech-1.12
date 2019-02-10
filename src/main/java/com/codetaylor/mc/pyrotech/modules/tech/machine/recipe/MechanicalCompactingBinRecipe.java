package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class MechanicalCompactingBinRecipe
    extends CompactingBinRecipeBase<MechanicalCompactingBinRecipe> {

  public MechanicalCompactingBinRecipe(ItemStack output, Ingredient input, int amount, int[] requiredToolUses) {

    super(output, input, amount, requiredToolUses);
  }

  @Nullable
  public static MechanicalCompactingBinRecipe getRecipe(ItemStack input) {

    for (MechanicalCompactingBinRecipe recipe : ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES, output);
  }
}
