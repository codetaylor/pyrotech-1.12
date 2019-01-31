package com.codetaylor.mc.pyrotech.modules.core.item;

import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemStoneHammer
    extends ItemTool {

  public static final String NAME = "stone_hammer";

  public ItemStoneHammer() {

    super(ToolMaterial.STONE, Collections.emptySet());
  }
}
