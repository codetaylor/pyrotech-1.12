package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemMushroomRedRoasted
    extends ItemFood {

  public static final String NAME = "mushroom_red_roasted";

  public ItemMushroomRedRoasted() {

    super(ModuleCoreConfig.FOOD.ROASTED_RED_MUSHROOM_HUNGER, (float) ModuleCoreConfig.FOOD.ROASTED_RED_MUSHROOM_SATURATION, false);
  }
}
