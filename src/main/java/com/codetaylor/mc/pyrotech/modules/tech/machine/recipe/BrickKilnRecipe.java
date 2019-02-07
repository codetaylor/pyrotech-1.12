package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.KilnRecipeBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class BrickKilnRecipe
    extends KilnRecipeBase<BrickKilnRecipe> {

  @Nullable
  public static BrickKilnRecipe getRecipe(ItemStack input) {

    for (BrickKilnRecipe recipe : ModuleTechMachine.Registries.BRICK_KILN_RECIPES) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.BRICK_KILN_RECIPES, output);
  }

  public BrickKilnRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      ItemStack[] failureItems
  ) {

    super(input, output, burnTimeTicks, failureChance, failureItems);
  }
}
