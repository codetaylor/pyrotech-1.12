package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;

public class ItemTongsFullDiamond
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_diamond_full";

  public ItemTongsFullDiamond() {

    super(() -> ModuleBloomery.Items.TONGS_DIAMOND, ModuleBloomeryConfig.TONGS.DIAMOND_TONGS_DURABILITY);
  }

}
