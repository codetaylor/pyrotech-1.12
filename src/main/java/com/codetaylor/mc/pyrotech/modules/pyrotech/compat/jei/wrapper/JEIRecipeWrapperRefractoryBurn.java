package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperRefractoryBurn
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;
  private final FluidStack fluidStack;

  public JEIRecipeWrapperRefractoryBurn(PitBurnRecipe recipe) {

    super(recipe);

    BlockMetaMatcher inputMatcher = recipe.getInputMatcher();
    Block block = inputMatcher.getBlock();

    int burnStages = recipe.getBurnStages();

    this.inputs = Collections.singletonList(Collections.singletonList(new ItemStack(Item.getItemFromBlock(block))));
    this.output = recipe.getOutput();
    this.output.setCount(burnStages);
    FluidStack fluidProduced = recipe.getFluidProduced();

    if (fluidProduced != null) {
      fluidProduced = fluidProduced.copy();
      fluidProduced.amount *= burnStages;
    }

    this.fluidStack = fluidProduced;
  }

  public FluidStack getFluidStack() {

    return this.fluidStack;
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(ItemStack.class, this.inputs);
    ingredients.setOutput(ItemStack.class, this.output);

    if (this.fluidStack != null) {
      ingredients.setOutput(FluidStack.class, this.fluidStack);
    }
  }
}
