package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemShears;

public class ItemStoneShears
    extends ItemShears {

  public static final String NAME = "stone_shears";

  public ItemStoneShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("stone"));
  }
}
