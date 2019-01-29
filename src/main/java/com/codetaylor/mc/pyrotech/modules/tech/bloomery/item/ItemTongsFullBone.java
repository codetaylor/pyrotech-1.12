package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;

public class ItemTongsFullBone
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_bone_full";

  public ItemTongsFullBone() {

    super(() -> ModuleBloomery.Items.TONGS_BONE, ModuleBloomeryConfig.TONGS.BONE_TONGS_DURABILITY);
  }

}
