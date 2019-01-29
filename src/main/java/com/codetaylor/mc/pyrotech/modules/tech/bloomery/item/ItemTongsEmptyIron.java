package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;

public class ItemTongsEmptyIron
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_iron";

  public ItemTongsEmptyIron() {

    super(() -> ModuleBloomery.Items.TONGS_IRON_FULL, ModuleBloomeryConfig.TONGS.IRON_TONGS_DURABILITY);
  }
}
