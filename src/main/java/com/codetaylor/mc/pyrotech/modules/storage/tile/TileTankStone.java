package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;

import javax.annotation.Nullable;

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

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleStorageConfig.STAGES_STONE_TANK;
  }
}
