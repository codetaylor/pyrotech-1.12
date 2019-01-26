package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;

public class TileCrateStone
    extends TileCrate {

  @Override
  protected int getMaxStacks() {

    return ModuleStorageConfig.DURABLE_CRATE.MAX_STACKS;
  }
}
