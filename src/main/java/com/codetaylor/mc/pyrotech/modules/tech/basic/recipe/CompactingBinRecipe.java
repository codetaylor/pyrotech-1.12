package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class CompactingBinRecipe
    extends CompactingBinRecipeBase<CompactingBinRecipe> {

  @Nullable
  public static CompactingBinRecipe getRecipe(ItemStack input) {

    for (CompactingBinRecipe recipe : ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE, output);
  }

  public CompactingBinRecipe(ItemStack output, Ingredient input, int amount) {

    super(output, input, amount, ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL);
  }

  public CompactingBinRecipe(ItemStack output, Ingredient input, int amount, int[] requiredToolUses) {

    super(output, input, amount, requiredToolUses);
  }
}
