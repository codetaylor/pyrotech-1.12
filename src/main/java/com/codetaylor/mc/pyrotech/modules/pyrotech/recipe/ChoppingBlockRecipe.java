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

  public ChoppingBlockRecipe(
      ItemStack output,
      Ingredient input
  ) {

    this.input = input;
    this.output = output;
    this.output.setCount(1); // Quantity is handled in the config file.
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
