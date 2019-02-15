package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;

public class ItemTongsEmptyFlint
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_flint";

  public ItemTongsEmptyFlint() {

    super(() -> ModuleTechBloomery.Items.TONGS_FLINT_FULL, ModuleTechBloomeryConfig.TONGS.FLINT_TONGS_DURABILITY);
  }
}
