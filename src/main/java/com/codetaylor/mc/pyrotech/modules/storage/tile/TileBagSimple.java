package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;

public class TileBagSimple
    extends TileBagBase {

  @Override
  public int getItemCapacity() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.MAX_ITEM_CAPACITY;
  }

  @Override
  public boolean allowAutomation() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOW_AUTOMATION;
  }
}
