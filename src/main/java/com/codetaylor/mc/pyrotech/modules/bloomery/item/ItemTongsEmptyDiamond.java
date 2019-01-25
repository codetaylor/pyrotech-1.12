package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsEmptyDiamond
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_diamond";

  public ItemTongsEmptyDiamond() {

    super(() -> ModuleItems.TONGS_DIAMOND_FULL, ModulePyrotechConfig.GENERAL.DIAMOND_TONGS_DURABILITY);
  }
}
