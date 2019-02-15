package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;

public class ItemTongsFullObsidian
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_obsidian_full";

  public ItemTongsFullObsidian() {

    super(() -> ModuleTechBloomery.Items.TONGS_OBSIDIAN, ModuleTechBloomeryConfig.TONGS.OBSIDIAN_TONGS_DURABILITY);
  }

}
