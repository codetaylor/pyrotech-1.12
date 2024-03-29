package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShieldBase;

public class ItemCrudeShield
    extends ItemShieldBase {

  public static final String NAME = "crude_shield";

  public ItemCrudeShield() {

    super(() -> ModuleToolConfig.CRUDE_SHIELD.DURABILITY);
  }
}
