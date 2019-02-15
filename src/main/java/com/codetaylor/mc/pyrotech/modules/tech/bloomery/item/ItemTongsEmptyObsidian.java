package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;

public class ItemTongsEmptyObsidian
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_obsidian";

  public ItemTongsEmptyObsidian() {

    super(() -> ModuleTechBloomery.Items.TONGS_OBSIDIAN_FULL, ModuleTechBloomeryConfig.TONGS.OBSIDIAN_TONGS_DURABILITY);
  }
}
