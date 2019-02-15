package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;

public class ItemTongsEmptyStone
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_stone";

  public ItemTongsEmptyStone() {

    super(() -> ModuleTechBloomery.Items.TONGS_STONE_FULL, ModuleTechBloomeryConfig.TONGS.STONE_TONGS_DURABILITY);
  }
}
