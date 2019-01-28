package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileTorchBase;

public class TileTorchStone
    extends TileTorchBase {

  @Override
  protected boolean isExtinguishedByRain() {

    return ModulePyrotechConfig.STONE_TORCH.EXTINGUISHED_BY_RAIN;
  }

  @Override
  protected boolean shouldBurnUp() {

    return ModulePyrotechConfig.STONE_TORCH.BURNS_UP;
  }

  @Override
  protected int getDuration() {

    return ModulePyrotechConfig.STONE_TORCH.DURATION;
  }

  @Override
  protected int getDurationVariant() {

    return ModulePyrotechConfig.STONE_TORCH.DURATION_VARIANT;
  }
}
