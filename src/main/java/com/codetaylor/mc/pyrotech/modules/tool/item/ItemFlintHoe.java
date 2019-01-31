package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemHoe;

public class ItemFlintHoe
    extends ItemHoe {

  public static final String NAME = "flint_hoe";

  public ItemFlintHoe() {

    super(EnumMaterial.FLINT.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("flint");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
