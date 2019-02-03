package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;

public class TileStashStone
    extends TileStash {

  @Override
  protected int getMaxStacks() {

    return ModuleStorageConfig.DURABLE_STASH.MAX_STACKS;
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleStorageConfig.DURABLE_STASH.ALLOW_AUTOMATION;
  }
}
