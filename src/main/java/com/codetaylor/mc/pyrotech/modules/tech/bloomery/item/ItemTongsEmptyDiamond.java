package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;

public class ItemTongsEmptyDiamond
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_diamond";

  public ItemTongsEmptyDiamond() {

    super(() -> ModuleTechBloomery.Items.TONGS_DIAMOND_FULL, ModuleTechBloomeryConfig.TONGS.DIAMOND_TONGS_DURABILITY);
  }
}
