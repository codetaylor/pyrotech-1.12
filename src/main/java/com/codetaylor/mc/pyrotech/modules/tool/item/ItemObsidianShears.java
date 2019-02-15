package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemShears;

public class ItemObsidianShears
    extends ItemShears {

  public static final String NAME = "obsidian_shears";

  public ItemObsidianShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("obsidian"));
  }
}
