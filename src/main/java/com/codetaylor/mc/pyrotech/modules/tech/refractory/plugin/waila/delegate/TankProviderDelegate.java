package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarTankBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TankProviderDelegate
    extends ProviderDelegateBase<TankProviderDelegate.ITankDisplay, TileTarTankBase> {

  public TankProviderDelegate(ITankDisplay display) {

    super(display);
  }

  @Override
  public void display(TileTarTankBase tile) {

    FluidTank fluidTank = tile.getFluidTank();
    FluidStack fluid = fluidTank.getFluid();

    if (fluid != null) {
      String langKey = "gui." + ModuleTechRefractory.MOD_ID + ".waila.tank.fluid";
      this.display.setFluid(langKey, fluid, fluidTank.getCapacity());

    } else {
      String langKey = "gui." + ModuleTechRefractory.MOD_ID + ".waila.tank.empty";
      this.display.setFluidEmpty(langKey, fluidTank.getCapacity());
    }
  }

  public interface ITankDisplay {

    void setFluid(String langKey, FluidStack fluid, int capacity);

    void setFluidEmpty(String langKey, int capacity);
  }
}
