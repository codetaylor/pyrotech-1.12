package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemShears;

public class ItemFlintShears
    extends ItemShears {

  public static final String NAME = "flint_shears";

  public ItemFlintShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("flint"));
  }
}
