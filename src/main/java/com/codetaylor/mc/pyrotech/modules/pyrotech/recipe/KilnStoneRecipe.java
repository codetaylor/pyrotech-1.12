package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class KilnStoneRecipe
    extends StoneMachineRecipeBase<KilnStoneRecipe> {

  @Nullable
  public static KilnStoneRecipe getRecipe(ItemStack input) {

    for (KilnStoneRecipe recipe : ModulePyrotechRegistries.KILN_STONE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.KILN_STONE_RECIPE, output);
  }

  private final float failureChance;
  private final ItemStack[] failureItems;

  public KilnStoneRecipe(
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
