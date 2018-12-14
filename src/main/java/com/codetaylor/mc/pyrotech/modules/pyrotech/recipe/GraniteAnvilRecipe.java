package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class GraniteAnvilRecipe
    extends IForgeRegistryEntry.Impl<GraniteAnvilRecipe>
    implements IRecipeSingleOutput {

  @Nullable
  public static GraniteAnvilRecipe getRecipe(ItemStack input) {

    for (GraniteAnvilRecipe recipe : ModulePyrotechRegistries.GRANITE_ANVIL_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.GRANITE_ANVIL_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int hits;

  public GraniteAnvilRecipe(
      ItemStack output,
      Ingredient input,
      int hits
  ) {

    this.input = input;
    this.output = output;
    this.hits = hits;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getHits() {

    return this.hits;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
