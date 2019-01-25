package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsEmptyStone
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_stone";

  public ItemTongsEmptyStone() {

    super(() -> ModuleItems.TONGS_STONE_FULL, ModulePyrotechConfig.GENERAL.STONE_TONGS_DURABILITY);
  }
}
