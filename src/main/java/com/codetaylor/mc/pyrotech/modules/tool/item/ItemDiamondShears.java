package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;

public class ItemDiamondShears
    extends ItemShearsBase {

  public static final String NAME = "diamond_shears";

  public ItemDiamondShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("diamond"));
  }
}
