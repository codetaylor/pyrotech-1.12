package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsFullFlint
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_flint_full";

  public ItemTongsFullFlint() {

    super(() -> ModuleItems.TONGS_FLINT, ModulePyrotechConfig.GENERAL.FLINT_TONGS_DURABILITY);
  }

}
