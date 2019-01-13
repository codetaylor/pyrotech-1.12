package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.item.ItemFood;

public class ItemAppleBaked
    extends ItemFood {

  public static final String NAME = "apple_baked";

  public ItemAppleBaked() {

    super(ModulePyrotechConfig.FOOD.BAKED_APPLE_AMOUNT, (float) ModulePyrotechConfig.FOOD.BAKED_APPLE_SATURATION, false);
  }
}
