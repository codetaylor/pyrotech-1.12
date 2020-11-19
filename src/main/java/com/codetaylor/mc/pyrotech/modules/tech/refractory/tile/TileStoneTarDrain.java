package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarDrainBase;

public class TileStoneTarDrain
    extends TileTarDrainBase {

  @Override
  protected int getHotFluidTemperature() {

    return ModuleTechRefractoryConfig.STONE_TAR_DRAIN.HOT_TEMPERATURE;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleTechRefractoryConfig.STONE_TAR_DRAIN.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected int getTankCapacity() {

    return ModuleTechRefractoryConfig.STONE_TAR_DRAIN.CAPACITY;
  }

  @Override
  protected int getDrainRange() {

    return ModuleTechRefractoryConfig.STONE_TAR_DRAIN.RANGE;
  }

  @Override
  protected boolean allowSourceDrain() {

    return ModuleTechRefractoryConfig.STONE_TAR_DRAIN.ALLOW_SOURCE_DRAIN;
  }

  @Override
  protected boolean allowTileDrain() {

    return ModuleTechRefractoryConfig.STONE_TAR_DRAIN.ALLOW_TILE_DRAIN;
  }
}
