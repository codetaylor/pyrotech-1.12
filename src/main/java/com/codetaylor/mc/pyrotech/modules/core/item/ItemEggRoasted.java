package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemEggRoasted
    extends ItemFood {

  public static final String NAME = "egg_roasted";

  public ItemEggRoasted() {

    super(ModuleCoreConfig.FOOD.ROASTED_EGG_HUNGER, (float) ModuleCoreConfig.FOOD.ROASTED_EGG_SATURATION, false);
  }
}
