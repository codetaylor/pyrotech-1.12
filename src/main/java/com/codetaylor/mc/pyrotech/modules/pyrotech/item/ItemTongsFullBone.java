package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsFullBone
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_bone_full";

  public ItemTongsFullBone() {

    super(() -> ModuleItems.TONGS_BONE, ModulePyrotechConfig.GENERAL.BONE_TONGS_DURABILITY);
  }

}
