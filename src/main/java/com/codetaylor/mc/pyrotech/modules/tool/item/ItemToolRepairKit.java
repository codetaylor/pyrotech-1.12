package com.codetaylor.mc.pyrotech.modules.tool.item;

import net.minecraft.item.Item;

public class ItemToolRepairKit
    extends Item {

  public static final String BONE_NAME = "bone_tool_repair_kit";
  public static final String FLINT_NAME = "flint_tool_repair_kit";

  public ItemToolRepairKit(int maxDamage) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxDamage);
  }

}
