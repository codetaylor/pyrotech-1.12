package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInItemOutBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class StoneKilnRecipe
    extends StoneMachineRecipeItemInItemOutBase<StoneKilnRecipe> {

  @Nullable
  public static StoneKilnRecipe getRecipe(ItemStack input) {

    for (StoneKilnRecipe recipe : ModuleTechMachine.Registries.KILN_STONE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.KILN_STONE_RECIPE, output);
  }

  private final float failureChance;
  private final ItemStack[] failureItems;

  public StoneKilnRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      ItemStack[] failureItems
  ) {

    super(input, output, burnTimeTicks);

    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.failureItems = failureItems;
  }

  public float getFailureChance() {

    return this.failureChance;
  }

  public ItemStack[] getFailureItems() {

    return this.failureItems;
  }

}
