package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;

public class ItemTongsFullFlint
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_flint_full";

  public ItemTongsFullFlint() {

    super(() -> ModuleTechBloomery.Items.TONGS_FLINT, ModuleTechBloomeryConfig.TONGS.FLINT_TONGS_DURABILITY);
  }

}
