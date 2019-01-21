package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsStoneFull
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_stone_full";

  public ItemTongsStoneFull() {

    super(() -> ModuleItems.TONGS_STONE, ModulePyrotechConfig.GENERAL.STONE_TONGS_DURABILITY);
  }
}
