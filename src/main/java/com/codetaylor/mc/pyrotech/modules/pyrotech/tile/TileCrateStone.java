package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class TileCrateStone
    extends TileCrate {

  @Override
  protected int getMaxStacks() {

    return ModulePyrotechConfig.DURABLE_CRATE.MAX_STACKS;
  }
}
