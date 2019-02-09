package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemBeetrootRoasted
    extends ItemFood {

  public static final String NAME = "beetroot_roasted";

  public ItemBeetrootRoasted() {

    super(ModuleCoreConfig.FOOD.ROASTED_BEETROOT_HUNGER, (float) ModuleCoreConfig.FOOD.ROASTED_BEETROOT_SATURATION, false);
  }
}
