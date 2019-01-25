package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;

public class ItemTongsFullFlint
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_flint_full";

  public ItemTongsFullFlint() {

    super(() -> ModuleBloomery.Items.TONGS_FLINT, ModuleBloomeryConfig.TONGS.FLINT_TONGS_DURABILITY);
  }

}
