package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;

public class ItemTongsFullDiamond
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_diamond_full";

  public ItemTongsFullDiamond() {

    super(() -> ModuleTechBloomery.Items.TONGS_DIAMOND, ModuleTechBloomeryConfig.TONGS.DIAMOND_TONGS_DURABILITY);
  }

}
