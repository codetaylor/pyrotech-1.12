package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemAxeBase;

public class ItemFlintAxe
    extends ItemAxeBase {

  public static final String NAME = "flint_axe";

  public ItemFlintAxe() {

    super(EnumMaterial.FLINT.getToolMaterial(), 8f, -3.2f);

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("flint");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
