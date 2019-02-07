package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBaseSawmill;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class BrickSawmillRecipe
    extends MachineRecipeBaseSawmill<BrickSawmillRecipe> {

  @Nullable
  public static BrickSawmillRecipe getRecipe(ItemStack input, ItemStack blade) {

    for (BrickSawmillRecipe recipe : ModuleTechMachine.Registries.BRICK_SAWMILL_RECIPES) {

      if (recipe.matches(input)
          && recipe.blade.apply(blade)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.BRICK_SAWMILL_RECIPES, output);
  }

  public BrickSawmillRecipe(
      ItemStack output,
      Ingredient input,
      int timeTicks,
      Ingredient blade,
      boolean createWoodChips
  ) {

    super(input, output, timeTicks, blade, createWoodChips);
  }
}
