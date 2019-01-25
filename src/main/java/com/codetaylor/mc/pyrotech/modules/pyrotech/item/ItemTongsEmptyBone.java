package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsEmptyBone
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_bone";

  public ItemTongsEmptyBone() {

    super(() -> ModuleItems.TONGS_BONE_FULL, ModulePyrotechConfig.GENERAL.BONE_TONGS_DURABILITY);
  }
}
