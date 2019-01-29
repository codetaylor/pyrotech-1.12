package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;

public class ItemTongsEmptyStone
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_stone";

  public ItemTongsEmptyStone() {

    super(() -> ModuleBloomery.Items.TONGS_STONE_FULL, ModuleBloomeryConfig.TONGS.STONE_TONGS_DURABILITY);
  }
}
