package com.codetaylor.mc.pyrotech.modules.ignition.tile;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.spi.TileTorchBase;

public class TileTorchStone
    extends TileTorchBase {

  @Override
  protected boolean isExtinguishedByRain() {

    return ModuleIgnitionConfig.STONE_TORCH.EXTINGUISHED_BY_RAIN;
  }

  @Override
  protected boolean shouldBurnUp() {

    return ModuleIgnitionConfig.STONE_TORCH.BURNS_UP;
  }

  @Override
  protected int getDuration() {

    return ModuleIgnitionConfig.STONE_TORCH.DURATION;
  }

  @Override
  protected int getDurationVariant() {

    return ModuleIgnitionConfig.STONE_TORCH.DURATION_VARIANT;
  }
}
