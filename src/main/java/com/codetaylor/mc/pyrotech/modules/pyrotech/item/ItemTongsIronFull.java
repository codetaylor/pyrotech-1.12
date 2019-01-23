package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;

public class ItemTongsIronFull
    extends ItemTongsFullBase {

  public static final String NAME = "tongs_iron_full";

  public ItemTongsIronFull() {

    super(() -> ModuleItems.TONGS_IRON, ModulePyrotechConfig.GENERAL.IRON_TONGS_DURABILITY);
  }

  @Override
  public int getDamagePerUse() {

    return ModulePyrotechConfig.GENERAL.IRON_TONGS_DAMAGE_PER_USE;
  }
}
