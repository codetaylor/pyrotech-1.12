package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;

public class TileTankStone
    extends TileTankBase {

  @Override
  protected int getTankCapacity() {

    return ModuleStorageConfig.STONE_TANK.CAPACITY;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleStorageConfig.STONE_TANK.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected int getHotFluidTemperature() {

    return ModuleStorageConfig.STONE_TANK.HOT_TEMPERATURE;
  }
}
