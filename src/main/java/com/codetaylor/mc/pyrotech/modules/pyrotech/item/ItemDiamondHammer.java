package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemDiamondHammer
    extends ItemTool {

  public static final String NAME = "diamond_hammer";

  public ItemDiamondHammer() {

    super(ToolMaterial.DIAMOND, Collections.emptySet());
  }
}
