package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class ChoppingBlockRecipe
    extends IForgeRegistryEntry.Impl<ChoppingBlockRecipe>
    implements IRecipeSingleOutput {

  @Nullable
  public static ChoppingBlockRecipe getRecipe(ItemStack input) {

    for (ChoppingBlockRecipe recipe : ModulePyrotechRegistries.CHOPPING_BLOCK_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.CHOPPING_BLOCK_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int chops;
  private final int minHarvestLevel;
  private final int maxHarvestLevel;

  public ChoppingBlockRecipe(
      ItemStack output,
      Ingredient input,
      int chops,
      int minHarvestLevel,
      int maxHarvestLevel
  ) {

    this.input = input;
    this.output = output;
    this.chops = chops;
    this.minHarvestLevel = minHarvestLevel;
    this.maxHarvestLevel = maxHarvestLevel;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getChops() {

    return this.chops;
  }

  public int getMinHarvestLevel() {

    return this.minHarvestLevel;
  }

  public int getMaxHarvestLevel() {

    return this.maxHarvestLevel;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
