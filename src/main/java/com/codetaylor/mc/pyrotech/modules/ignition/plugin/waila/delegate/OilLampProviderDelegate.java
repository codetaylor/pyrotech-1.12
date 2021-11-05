package com.codetaylor.mc.pyrotech.modules.ignition.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.TileLampOil;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class OilLampProviderDelegate
    extends ProviderDelegateBase<OilLampProviderDelegate.ITankDisplay, TileLampOil> {

  public OilLampProviderDelegate(ITankDisplay display) {

    super(display);
  }

  @Override
  public void display(TileLampOil tile) {

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
