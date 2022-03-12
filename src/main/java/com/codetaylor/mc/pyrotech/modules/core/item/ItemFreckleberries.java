package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemFreckleberries
    extends ItemFood {

  public static final String NAME = "freckleberries";

  public ItemFreckleberries() {

    super(ModuleCoreConfig.FOOD.FRECKLEBERRIES_HUNGER, (float) ModuleCoreConfig.FOOD.FRECKLEBERRIES_SATURATION, false);
  }
}
