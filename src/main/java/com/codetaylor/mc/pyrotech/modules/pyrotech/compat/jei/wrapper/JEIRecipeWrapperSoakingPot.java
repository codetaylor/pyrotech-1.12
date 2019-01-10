package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.SoakingPotRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIRecipeWrapperSoakingPot
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final FluidStack inputFluid;
  private final ItemStack output;

  public JEIRecipeWrapperSoakingPot(SoakingPotRecipe recipe) {

    super(recipe);

    this.inputs = new ArrayList<>(2);
    this.inputs.add(Arrays.asList(recipe.getInputItem().getMatchingStacks()));
    this.inputFluid = recipe.getInputFluid();

    this.output = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setInput(VanillaTypes.FLUID, this.inputFluid);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }
}
