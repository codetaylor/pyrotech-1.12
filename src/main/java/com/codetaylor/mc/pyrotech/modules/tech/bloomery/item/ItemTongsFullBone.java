package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;

public class ItemTongsFullBone
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_bone_full";

  public ItemTongsFullBone() {

    super(() -> ModuleTechBloomery.Items.TONGS_BONE, ModuleTechBloomeryConfig.TONGS.BONE_TONGS_DURABILITY);
  }

}
