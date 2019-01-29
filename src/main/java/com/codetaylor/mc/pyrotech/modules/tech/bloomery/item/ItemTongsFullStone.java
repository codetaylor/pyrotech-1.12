package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;

public class ItemTongsFullStone
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_stone_full";

  public ItemTongsFullStone() {

    super(() -> ModuleBloomery.Items.TONGS_STONE, ModuleBloomeryConfig.TONGS.STONE_TONGS_DURABILITY);
  }
}
