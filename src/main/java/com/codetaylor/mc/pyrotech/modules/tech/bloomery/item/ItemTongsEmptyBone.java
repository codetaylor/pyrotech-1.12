package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;

public class ItemTongsEmptyBone
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_bone";

  public ItemTongsEmptyBone() {

    super(() -> ModuleTechBloomery.Items.TONGS_BONE_FULL, ModuleTechBloomeryConfig.TONGS.BONE_TONGS_DURABILITY);
  }
}
