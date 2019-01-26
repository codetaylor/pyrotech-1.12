package com.codetaylor.mc.pyrotech.library.fluid;

import net.minecraftforge.fluids.FluidTank;

// TODO: switch to tile data tank and remove this

public interface IFluidTankUpdateListener {

  void onTankContentsChanged(FluidTank fluidTank);
}
