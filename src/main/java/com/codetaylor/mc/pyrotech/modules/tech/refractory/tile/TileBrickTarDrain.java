package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarDrainBase;

public class TileBrickTarDrain
    extends TileTarDrainBase {

  @Override
  protected int getHotFluidTemperature() {

    return ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.HOT_TEMPERATURE;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected int getTankCapacity() {

    return ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.CAPACITY;
  }
}
