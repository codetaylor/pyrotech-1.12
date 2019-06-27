package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;

import javax.annotation.Nullable;

public class TileTankBrick
    extends TileTankBase {

  @Override
  protected int getTankCapacity() {

    return ModuleStorageConfig.BRICK_TANK.CAPACITY;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleStorageConfig.BRICK_TANK.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected int getHotFluidTemperature() {

    return ModuleStorageConfig.BRICK_TANK.HOT_TEMPERATURE;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleStorageConfig.STAGES_BRICK_TANK;
  }
}
