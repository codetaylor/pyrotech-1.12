package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;

public class TileShelfStone
    extends TileShelf {

  @Override
  protected int getMaxStacks() {

    return ModuleStorageConfig.DURABLE_SHELF.MAX_STACKS;
  }
}
