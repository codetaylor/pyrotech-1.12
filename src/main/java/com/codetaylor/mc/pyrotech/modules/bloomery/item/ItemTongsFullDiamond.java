package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsFullDiamond
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_diamond_full";

  public ItemTongsFullDiamond() {

    super(() -> ModuleItems.TONGS_DIAMOND, ModulePyrotechConfig.GENERAL.DIAMOND_TONGS_DURABILITY);
  }

}
