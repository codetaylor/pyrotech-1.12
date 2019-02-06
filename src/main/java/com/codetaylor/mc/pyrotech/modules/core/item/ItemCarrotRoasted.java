package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemCarrotRoasted
    extends ItemFood {

  public static final String NAME = "carrot_roasted";

  public ItemCarrotRoasted() {

    super(ModuleCoreConfig.FOOD.ROASTED_CARROT_HUNGER, (float) ModuleCoreConfig.FOOD.ROASTED_CARROT_SATURATION, false);
  }
}
