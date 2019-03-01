package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemHammerBase;

import java.util.Collections;

public class ItemCrudeHammer
    extends ItemHammerBase {

  public static final String NAME = "crude_hammer";

  public ItemCrudeHammer() {

    super(ToolMaterial.STONE, Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.CRUDE_HAMMER_DURABILITY);
  }
}
