package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleFluidOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class BarrelRecipe
    extends IForgeRegistryEntry.Impl<BarrelRecipe>
    implements IRecipeSingleFluidOutput,
    IRecipeTimed {

  @Nullable
  public static BarrelRecipe getRecipe(ItemStack[] inputItems, FluidStack inputFluid) {

    for (BarrelRecipe recipe : ModuleTechBasic.Registries.BARREL_RECIPE) {

      if (recipe.matches(inputItems, inputFluid)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(FluidStack output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.BARREL_RECIPE, output);
  }

  public static boolean isValidItem(ItemStack itemStack, FluidStack inputFluid) {

    for (BarrelRecipe recipe : ModuleTechBasic.Registries.BARREL_RECIPE) {

      if (recipe.inputFluid.isFluidEqual(inputFluid)
          && recipe.isValidItem(itemStack)) {
        return true;
      }
    }

    return false;
  }

  private final FluidStack output;
  private final Ingredient[] inputItems;
  private final FluidStack inputFluid;
  private final int timeTicks;

  public BarrelRecipe(
      FluidStack output,
      Ingredient[] inputItems,
      FluidStack inputFluid,
      int timeTicks
  ) {

    this.output = output;
    this.inputItems = inputItems;
    this.inputFluid = inputFluid;
    this.timeTicks = timeTicks;
  }

  public Ingredient[] getInputItems() {

    return this.inputItems;
  }

  public FluidStack getInputFluid() {

    return this.inputFluid;
  }

  public FluidStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return (int) Math.max(1, this.timeTicks * ModuleTechBasicConfig.BARREL.BASE_RECIPE_DURATION_MODIFIER);
  }

  public boolean isValidItem(ItemStack inputItem) {

    for (int i = 0; i < this.inputItems.length; i++) {

      if (this.inputItems[i].apply(inputItem)) {
        return true;
      }
    }

    return false;
  }

  public boolean matches(ItemStack[] inputItems, FluidStack inputFluid) {

    if (!this.inputFluid.isFluidEqual(inputFluid)) {
      return false;
    }

    int flags = 0;
    int matched = 0;

    outer:
    for (int i = 0; i < inputItems.length; i++) {

      if (inputItems[i].isEmpty()) {
        continue;
      }

      for (int j = 0; j < this.inputItems.length; j++) {

        if ((flags & (1 << j)) == (1 << j)) {
          continue;
        }

        if (this.inputItems[j].apply(inputItems[i])) {
          flags |= (1 << j);
          matched += 1;
          continue outer;
        }
      }

      return false;
    }

    return (matched == this.inputItems.length);
  }
}
