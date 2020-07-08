package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
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

    for (SoakingPotRecipe recipe : ModuleTechBasic.Registries.SOAKING_POT_RECIPE) {

      if (recipe.matches(inputItem, inputFluid)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.SOAKING_POT_RECIPE, output);
  }

  private final ItemStack output;
  private final Ingredient inputItem;
  private final FluidStack inputFluid;
  private final boolean campfireRequired;
  private final int timeTicks;

  public SoakingPotRecipe(
      ItemStack output,
      Ingredient inputItem,
      FluidStack inputFluid,
      boolean campfireRequired,
      int timeTicks
  ) {

    this.output = output;
    this.inputItem = inputItem;
    this.inputFluid = inputFluid;
    this.campfireRequired = campfireRequired;
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

  public boolean isCampfireRequired() {

    return this.campfireRequired;
  }

  @Override
  public int getTimeTicks() {

    return (int) Math.max(1, this.timeTicks * ModuleTechBasicConfig.SOAKING_POT.BASE_RECIPE_DURATION_MODIFIER);
  }

  public boolean matches(ItemStack inputItem, FluidStack inputFluid) {

    return this.inputItem.apply(inputItem)
        && this.inputFluid.isFluidEqual(inputFluid);
  }
}
