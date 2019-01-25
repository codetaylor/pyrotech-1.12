package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;

public class ItemTongsEmptyFlint
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_flint";

  public ItemTongsEmptyFlint() {

    super(() -> ModuleBloomery.Items.TONGS_FLINT_FULL, ModuleBloomeryConfig.TONGS.FLINT_TONGS_DURABILITY);
  }
}
