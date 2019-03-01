package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShovelBase;

public class ItemFlintShovel
    extends ItemShovelBase {

  public static final String NAME = "flint_shovel";

  public ItemFlintShovel() {

    super(EnumMaterial.FLINT.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("flint");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
