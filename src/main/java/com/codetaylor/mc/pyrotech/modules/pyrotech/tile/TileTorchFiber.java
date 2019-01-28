package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class TileTorchFiber
    extends TileTorchBase {

  @Override
  protected boolean isExtinguishedByRain() {

    return ModulePyrotechConfig.FIBER_TORCH.EXTINGUISHED_BY_RAIN;
  }

  @Override
  protected boolean shouldBurnUp() {

    return ModulePyrotechConfig.FIBER_TORCH.BURNS_UP;
  }

  @Override
  protected int getDuration() {

    return ModulePyrotechConfig.FIBER_TORCH.DURATION;
  }

  @Override
  protected int getDurationVariant() {

    return ModulePyrotechConfig.FIBER_TORCH.DURATION_VARIANT;
  }
}
