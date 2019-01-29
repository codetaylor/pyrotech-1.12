package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomeryConfig;

public class ItemTongsEmptyBone
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_bone";

  public ItemTongsEmptyBone() {

    super(() -> ModuleBloomery.Items.TONGS_BONE_FULL, ModuleBloomeryConfig.TONGS.BONE_TONGS_DURABILITY);
  }
}
