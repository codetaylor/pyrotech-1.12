package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsIron
    extends ItemTongsBase {

  public static final String NAME = "tongs_iron";

  public ItemTongsIron() {

    super(() -> ModuleItems.TONGS_IRON_FULL, ModulePyrotechConfig.GENERAL.IRON_TONGS_DURABILITY);
  }
}
