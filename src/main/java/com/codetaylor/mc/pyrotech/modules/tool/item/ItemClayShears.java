package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;

public class ItemClayShears
    extends ItemShearsBase {

  public static final String NAME = "clay_shears";

  public ItemClayShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("clay"));
  }
}
