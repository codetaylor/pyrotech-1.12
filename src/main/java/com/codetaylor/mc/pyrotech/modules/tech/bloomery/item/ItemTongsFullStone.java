package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;

public class ItemTongsFullStone
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_stone_full";

  public ItemTongsFullStone() {

    super(() -> ModuleTechBloomery.Items.TONGS_STONE, ModuleTechBloomeryConfig.TONGS.STONE_TONGS_DURABILITY);
  }
}
