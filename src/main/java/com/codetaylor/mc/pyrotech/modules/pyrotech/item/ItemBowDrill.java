package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.item.ItemStack;

public class ItemBowDrill
    extends ItemIgniterBase {

  public static final String NAME = "bow_drill";

  public ItemBowDrill() {

    this.setMaxDamage(ModulePyrotechConfig.GENERAL.BOW_DRILL_DURABILITY);
    this.setMaxStackSize(1);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return ModulePyrotechConfig.GENERAL.BOW_DRILL_USE_DURATION;
  }

}
