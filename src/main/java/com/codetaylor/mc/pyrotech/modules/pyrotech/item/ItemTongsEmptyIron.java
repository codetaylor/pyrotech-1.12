package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsEmptyIron
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_iron";

  public ItemTongsEmptyIron() {

    super(() -> ModuleItems.TONGS_IRON_FULL, ModulePyrotechConfig.GENERAL.IRON_TONGS_DURABILITY);
  }
}
