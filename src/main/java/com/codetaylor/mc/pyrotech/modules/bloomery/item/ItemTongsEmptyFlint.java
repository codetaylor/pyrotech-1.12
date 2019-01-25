package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsEmptyFlint
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_flint";

  public ItemTongsEmptyFlint() {

    super(() -> ModuleItems.TONGS_FLINT_FULL, ModulePyrotechConfig.GENERAL.FLINT_TONGS_DURABILITY);
  }
}
