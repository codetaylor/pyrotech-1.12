package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShieldBase;

public class ItemDurableShield
    extends ItemShieldBase {

  public static final String NAME = "durable_shield";

  public ItemDurableShield() {

    super(() -> ModuleToolConfig.DURABLE_SHIELD.DURABILITY);
  }
}
