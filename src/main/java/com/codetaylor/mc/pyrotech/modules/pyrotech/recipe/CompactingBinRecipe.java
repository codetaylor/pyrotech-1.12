package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class CompactingBinRecipe
    extends IForgeRegistryEntry.Impl<CompactingBinRecipe>
    implements IRecipeSingleOutput {

  @Nullable
  public static CompactingBinRecipe getRecipe(ItemStack input) {

    for (CompactingBinRecipe recipe : ModulePyrotechRegistries.COMPACTING_BIN_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.COMPACTING_BIN_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int amount;

  public CompactingBinRecipe(
      ItemStack output,
      Ingredient input,
      int amount
  ) {

    this.input = input;
    this.output = output;
    this.amount = amount;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getAmount() {

    return this.amount;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
