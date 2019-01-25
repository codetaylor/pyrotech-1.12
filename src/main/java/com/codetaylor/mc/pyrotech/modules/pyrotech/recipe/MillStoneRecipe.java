package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class MillStoneRecipe
    extends StoneMachineRecipeItemInItemOutBase<MillStoneRecipe> {

  private final Ingredient blade;
  private final boolean createWoodChips;

  @Nullable
  public static MillStoneRecipe getRecipe(ItemStack input, ItemStack blade) {

    for (MillStoneRecipe recipe : ModPyrotechRegistries.MILL_STONE_RECIPE) {

      if (recipe.matches(input)
          && recipe.blade.apply(blade)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModPyrotechRegistries.MILL_STONE_RECIPE, output);
  }

  public MillStoneRecipe(
      ItemStack output,
      Ingredient input,
      int timeTicks,
      Ingredient blade,
      boolean createWoodChips
  ) {

    super(input, output, timeTicks);
    this.blade = blade;
    this.createWoodChips = createWoodChips;
  }

  public Ingredient getBlade() {

    return this.blade;
  }

  public boolean createWoodChips() {

    return this.createWoodChips;
  }
}
