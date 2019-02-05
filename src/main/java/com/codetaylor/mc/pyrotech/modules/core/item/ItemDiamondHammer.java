package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemDiamondHammer
    extends ItemTool {

  public static final String NAME = "diamond_hammer";

  public ItemDiamondHammer() {

    super(ToolMaterial.DIAMOND, Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.DIAMOND_HAMMER_DURABILITY);
  }
}
