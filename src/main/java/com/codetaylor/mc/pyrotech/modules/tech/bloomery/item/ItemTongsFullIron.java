package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;

public class ItemTongsFullIron
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_iron_full";

  public ItemTongsFullIron() {

    super(() -> ModuleTechBloomery.Items.TONGS_IRON, ModuleTechBloomeryConfig.TONGS.IRON_TONGS_DURABILITY);
  }

}
