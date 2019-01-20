package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class BloomeryRecipe
    extends IForgeRegistryEntry.Impl<BloomeryRecipe>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  @Nullable
  public static BloomeryRecipe getRecipe(ItemStack input) {

    for (BloomeryRecipe recipe : ModulePyrotechRegistries.BLOOMERY_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.BLOOMERY_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int burnTimeTicks;
  private final float failureChance;
  private final ItemStack[] failureItems;

  public BloomeryRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      ItemStack[] failureItems
  ) {

    this.input = input;
    this.output = output;
    this.burnTimeTicks = burnTimeTicks;
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.failureItems = failureItems;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return this.burnTimeTicks;
  }

  public float getFailureChance() {

    return this.failureChance;
  }

  public ItemStack[] getFailureItems() {

    return this.failureItems;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
