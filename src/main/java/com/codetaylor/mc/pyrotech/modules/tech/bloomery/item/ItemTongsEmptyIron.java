package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;

public class ItemTongsEmptyIron
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_iron";

  public ItemTongsEmptyIron() {

    super(() -> ModuleTechBloomery.Items.TONGS_IRON_FULL, ModuleTechBloomeryConfig.TONGS.IRON_TONGS_DURABILITY);
  }
}
