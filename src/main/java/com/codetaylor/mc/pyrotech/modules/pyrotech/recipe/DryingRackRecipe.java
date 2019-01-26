package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class DryingRackRecipe
    extends IForgeRegistryEntry.Impl<DryingRackRecipe>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  @Nullable
  public static DryingRackRecipe getRecipe(ItemStack input) {

    for (DryingRackRecipe recipe : ModPyrotechRegistries.DRYING_RACK_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModPyrotechRegistries.DRYING_RACK_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int dryTimeTicks;

  public DryingRackRecipe(
      ItemStack output,
      Ingredient input,
      int dryTimeTicks
  ) {

    this.input = input;
    this.output = output;
    this.dryTimeTicks = dryTimeTicks;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return this.dryTimeTicks;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
