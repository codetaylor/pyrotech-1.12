package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemPickaxeBase;

public class ItemFlintPickaxe
    extends ItemPickaxeBase {

  public static final String NAME = "flint_pickaxe";

  public ItemFlintPickaxe() {

    super(EnumMaterial.FLINT.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("flint");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
