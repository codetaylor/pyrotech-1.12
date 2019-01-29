package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;

public class ItemTongsFullIron
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_iron_full";

  public ItemTongsFullIron() {

    super(() -> ModuleBloomery.Items.TONGS_IRON, ModuleBloomeryConfig.TONGS.IRON_TONGS_DURABILITY);
  }

}
