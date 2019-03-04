package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;

public class ItemObsidianShears
    extends ItemShearsBase {

  public static final String NAME = "obsidian_shears";

  public ItemObsidianShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("obsidian"));
  }
}