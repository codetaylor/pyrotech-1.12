package com.codetaylor.mc.pyrotech.modules.ignition.tile;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.spi.TileTorchBase;

public class TileTorchFiber
    extends TileTorchBase {

  @Override
  protected boolean isExtinguishedByRain() {

    return ModuleIgnitionConfig.FIBER_TORCH.EXTINGUISHED_BY_RAIN;
  }

  @Override
  protected boolean shouldBurnUp() {

    return ModuleIgnitionConfig.FIBER_TORCH.BURNS_UP;
  }

  @Override
  protected int getDuration() {

    return ModuleIgnitionConfig.FIBER_TORCH.DURATION;
  }

  @Override
  protected int getDurationVariant() {

    return ModuleIgnitionConfig.FIBER_TORCH.DURATION_VARIANT;
  }
}
