package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.ModuleMaterials;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemSwordBase;
import com.google.common.base.Preconditions;

public class ItemObsidianSword
    extends ItemSwordBase {

  public static final String NAME = "obsidian_sword";

  public ItemObsidianSword() {

    super(Preconditions.checkNotNull(ModuleMaterials.OBSIDIAN));

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("obsidian");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }
}
