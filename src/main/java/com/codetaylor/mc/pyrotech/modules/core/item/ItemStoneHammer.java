package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemStoneHammer
    extends ItemTool {

  public static final String NAME = "stone_hammer";

  public ItemStoneHammer() {

    super(ToolMaterial.STONE, Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.STONE_HAMMER_DURABILITY);
  }
}
