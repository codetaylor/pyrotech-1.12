package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInItemOutBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class StoneSawmillRecipe
    extends StoneMachineRecipeItemInItemOutBase<StoneSawmillRecipe> {

  private final Ingredient blade;
  private final boolean createWoodChips;

  @Nullable
  public static StoneSawmillRecipe getRecipe(ItemStack input, ItemStack blade) {

    for (StoneSawmillRecipe recipe : ModuleTechMachine.Registries.MILL_STONE_RECIPE) {

      if (recipe.matches(input)
          && recipe.blade.apply(blade)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.MILL_STONE_RECIPE, output);
  }

  public StoneSawmillRecipe(
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
