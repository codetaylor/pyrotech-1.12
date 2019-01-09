package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class TileStashStone
    extends TileStash {

  @Override
  protected int getMaxStacks() {

    return ModulePyrotechConfig.DURABLE_STASH.MAX_STACKS;
  }
}
