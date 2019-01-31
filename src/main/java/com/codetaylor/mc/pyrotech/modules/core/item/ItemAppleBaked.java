package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemAppleBaked
    extends ItemFood {

  public static final String NAME = "apple_baked";

  public ItemAppleBaked() {

    super(ModuleCoreConfig.FOOD.BAKED_APPLE_HUNGER, (float) ModuleCoreConfig.FOOD.BAKED_APPLE_SATURATION, false);
  }
}
