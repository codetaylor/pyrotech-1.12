package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemShears;

public class ItemClayShears
    extends ItemShears {

  public static final String NAME = "clay_shears";

  public ItemClayShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("clay"));
  }
}
