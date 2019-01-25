package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import com.codetaylor.mc.pyrotech.spi.recipe.IRecipeTimed;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class SoakingPotRecipe
    extends IForgeRegistryEntry.Impl<SoakingPotRecipe>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  @Nullable
  public static SoakingPotRecipe getRecipe(ItemStack inputItem, FluidStack inputFluid) {

    for (SoakingPotRecipe recipe : ModPyrotechRegistries.SOAKING_POT_RECIPE) {

      if (recipe.matches(inputItem, inputFluid)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModPyrotechRegistries.SOAKING_POT_RECIPE, output);
  }

  private final ItemStack output;
  private final Ingredient inputItem;
  private final FluidStack inputFluid;
  private final int timeTicks;

  public SoakingPotRecipe(
      ItemStack output,
      Ingredient inputItem,
      FluidStack inputFluid,
      int timeTicks
  ) {

    this.output = output;
    this.inputItem = inputItem;
    this.inputFluid = inputFluid;
    this.timeTicks = timeTicks;
  }

  public Ingredient getInputItem() {

    return this.inputItem;
  }

  public FluidStack getInputFluid() {

    return this.inputFluid;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return this.timeTicks;
  }

  public boolean matches(ItemStack inputItem, FluidStack inputFluid) {

    return this.inputItem.apply(inputItem)
        && this.inputFluid.isFluidEqual(inputFluid);
  }
}
