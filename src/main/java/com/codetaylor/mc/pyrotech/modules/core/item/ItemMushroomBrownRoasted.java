package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemMushroomBrownRoasted
    extends ItemFood {

  public static final String NAME = "mushroom_brown_roasted";

  public ItemMushroomBrownRoasted() {

    super(ModuleCoreConfig.FOOD.ROASTED_BROWN_MUSHROOM_HUNGER, (float) ModuleCoreConfig.FOOD.ROASTED_BROWN_MUSHROOM_SATURATION, false);
  }
}
