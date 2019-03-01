package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;

public class ItemStoneShears
    extends ItemShearsBase {

  public static final String NAME = "stone_shears";

  public ItemStoneShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("stone"));
  }
}
