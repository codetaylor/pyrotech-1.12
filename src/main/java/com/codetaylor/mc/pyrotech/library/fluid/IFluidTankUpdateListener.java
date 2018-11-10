package com.codetaylor.mc.pyrotech.library.fluid;

import net.minecraftforge.fluids.FluidTank;

public interface IFluidTankUpdateListener {

  void onTankContentsChanged(FluidTank fluidTank);
}
