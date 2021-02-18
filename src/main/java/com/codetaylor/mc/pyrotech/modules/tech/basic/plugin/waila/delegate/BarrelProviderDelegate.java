package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileBarrel;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelProviderDelegate
    extends ProviderDelegateBase<BarrelProviderDelegate.IBarrelDisplay, TileBarrel> {

  public BarrelProviderDelegate(IBarrelDisplay display) {

    super(display);
  }

  @Override
  public void display(TileBarrel tile) {

    BarrelRecipe currentRecipe = tile.getCurrentRecipe();
    TileBarrel.InputStackHandler inputStackHandler = tile.getInputStackHandler();
    TileBarrel.InputFluidTank inputFluidTank = tile.getInputFluidTank();
    FluidStack inputFluid = inputFluidTank.getFluid();

    if (currentRecipe != null) {
      float progress = tile.getRecipeProgress();
      FluidStack outputFluid = currentRecipe.getOutput();
      this.display.setRecipeProgress(inputStackHandler, inputFluid, outputFluid, (int) (100 * progress), 100);

    } else {
      this.display.setRecipeProgress(inputStackHandler, inputFluid, null, 0, 100);
    }

    if (inputFluid != null) {
      this.display.setFluid(inputFluid, inputFluidTank.getCapacity());
    }
  }

  public interface IBarrelDisplay {

    void setRecipeProgress(ItemStackHandler inputStackHandler, FluidStack inputFluid, FluidStack outputFluid, int progress, int maxProgress);

    void setFluid(FluidStack fluidStack, int capacity);
  }
}
