package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileSoakingPot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SoakingPotProviderDelegate
    extends ProviderDelegateBase<SoakingPotProviderDelegate.ISoakingPotDisplay, TileSoakingPot> {

  public SoakingPotProviderDelegate(ISoakingPotDisplay display) {

    super(display);
  }

  @Override
  public void display(TileSoakingPot tile) {

    SoakingPotRecipe currentRecipe = tile.getCurrentRecipe();
    TileSoakingPot.InputStackHandler inputStackHandler = tile.getInputStackHandler();
    ItemStack inputStack = inputStackHandler.getStackInSlot(0);
    TileSoakingPot.InputFluidTank inputFluidTank = tile.getInputFluidTank();
    FluidStack fluid = inputFluidTank.getFluid();

    if (currentRecipe != null) {

      float progress = tile.getRecipeProgress();
      ItemStack outputStack = currentRecipe.getOutput();
      outputStack.setCount(inputStack.getCount() * outputStack.getCount());

      this.display.setRecipeProgress(inputStack, outputStack, (int) (100 * progress), 100);
    }

    if (fluid != null) {
      this.display.setFluid(fluid, inputFluidTank.getCapacity());
    }

    TileSoakingPot.OutputStackHandler outputStackHandler = tile.getOutputStackHandler();
    ItemStack outputStack = outputStackHandler.getStackInSlot(0);

    if (!outputStack.isEmpty()) {
      this.display.setOutputItem(outputStack);
    }
  }

  public interface ISoakingPotDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setOutputItem(ItemStack outputStack);

    void setFluid(FluidStack fluidStack, int capacity);
  }
}
