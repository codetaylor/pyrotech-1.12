package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;

public class ItemTongsFullGold
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_gold_full";

  public ItemTongsFullGold() {

    super(() -> ModuleTechBloomery.Items.TONGS_GOLD, ModuleTechBloomeryConfig.TONGS.GOLD_TONGS_DURABILITY);
  }

}
