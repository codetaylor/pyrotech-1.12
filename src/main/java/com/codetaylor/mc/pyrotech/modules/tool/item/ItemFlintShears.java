package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;

public class ItemFlintShears
    extends ItemShearsBase {

  public static final String NAME = "flint_shears";

  public ItemFlintShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("flint"));
  }
}
