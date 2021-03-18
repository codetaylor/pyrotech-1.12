package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemGloamberries
    extends ItemFood {

  public static final String NAME = "gloamberries";

  public ItemGloamberries() {

    super(ModuleCoreConfig.FOOD.GLOAMBERRIES_HUNGER, (float) ModuleCoreConfig.FOOD.GLOAMBERRIES_SATURATION, false);
    this.setAlwaysEdible();
  }
}
