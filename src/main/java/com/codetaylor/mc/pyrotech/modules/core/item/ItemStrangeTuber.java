package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemFood;

public class ItemStrangeTuber
    extends ItemFood {

  public static final String NAME = "strange_tuber";

  public ItemStrangeTuber() {

    super(ModuleCoreConfig.FOOD.STRANGE_TUBER_HUNGER, (float) ModuleCoreConfig.FOOD.STRANGE_TUBER_SATURATION, false);
  }
}
