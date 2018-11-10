package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.item.ItemStack;

public class ItemFlintAndTinder
    extends ItemIgniterBase {

  public static final String NAME = "flint_and_tinder";

  public ItemFlintAndTinder() {

    this.setMaxDamage(ModulePyrotechConfig.GENERAL.FLINT_AND_TINDER_DURABILITY);
    this.setMaxStackSize(1);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return ModulePyrotechConfig.GENERAL.FLINT_AND_TINDER_USE_DURATION;
  }

}
