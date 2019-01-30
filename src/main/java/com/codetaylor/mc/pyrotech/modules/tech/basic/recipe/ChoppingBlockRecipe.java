package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class ChoppingBlockRecipe
    extends IForgeRegistryEntry.Impl<ChoppingBlockRecipe>
    implements IRecipeSingleOutput {

  @Nullable
  public static ChoppingBlockRecipe getRecipe(ItemStack input) {

    for (ChoppingBlockRecipe recipe : ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int[] chops;
  private final int[] quantities;

  public ChoppingBlockRecipe(ItemStack output, Ingredient input) {

    this(output, input, ModuleTechBasicConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL, ModuleTechBasicConfig.CHOPPING_BLOCK.RECIPE_RESULT_QUANTITY_PER_HARVEST_LEVEL);
  }

  public ChoppingBlockRecipe(
      ItemStack output,
      Ingredient input,
      int[] chops,
      int[] quantities
  ) {

    this.input = input;
    this.output = output;
    this.chops = chops;
    this.quantities = quantities;
    this.output.setCount(1); // Quantity is managed.
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int[] getChops() {

    return this.chops;
  }

  public int[] getQuantities() {

    return this.quantities;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
