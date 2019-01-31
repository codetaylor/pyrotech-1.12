package com.codetaylor.mc.pyrotech.modules.core.item;

import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemIronHammer
    extends ItemTool {

  public static final String NAME = "iron_hammer";

  public ItemIronHammer() {

    super(ToolMaterial.IRON, Collections.emptySet());
  }
}
