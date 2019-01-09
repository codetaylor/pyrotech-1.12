package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class TileShelfStone
    extends TileShelf {

  @Override
  protected int getMaxStacks() {

    return ModulePyrotechConfig.DURABLE_SHELF.MAX_STACKS;
  }
}
