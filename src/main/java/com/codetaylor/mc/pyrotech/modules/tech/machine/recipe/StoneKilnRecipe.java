package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.KilnRecipeBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class StoneKilnRecipe
    extends KilnRecipeBase<StoneKilnRecipe> {

  @Nullable
  public static StoneKilnRecipe getRecipe(ItemStack input) {

    for (StoneKilnRecipe recipe : ModuleTechMachine.Registries.STONE_KILN_RECIPES) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.STONE_KILN_RECIPES, output);
  }

  public StoneKilnRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      ItemStack[] failureItems
  ) {

    super(input, output, burnTimeTicks, failureChance, failureItems);
  }

}
