package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.ModuleMaterials;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemAxeBase;
import com.google.common.base.Preconditions;

public class ItemObsidianAxe
    extends ItemAxeBase {

  public static final String NAME = "obsidian_axe";

  public ItemObsidianAxe() {

    super(Preconditions.checkNotNull(ModuleMaterials.OBSIDIAN), 8f, -3.0f);

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("obsidian");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
