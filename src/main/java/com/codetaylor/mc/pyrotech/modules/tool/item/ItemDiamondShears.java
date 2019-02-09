package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemShears;

public class ItemDiamondShears
    extends ItemShears {

  public static final String NAME = "diamond_shears";

  public ItemDiamondShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("diamond"));
  }
}
