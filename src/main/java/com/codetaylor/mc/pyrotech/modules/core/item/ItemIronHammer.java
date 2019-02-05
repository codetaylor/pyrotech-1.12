package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemIronHammer
    extends ItemTool {

  public static final String NAME = "iron_hammer";

  public ItemIronHammer() {

    super(ToolMaterial.IRON, Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.IRON_HAMMER_DURABILITY);
  }
}
