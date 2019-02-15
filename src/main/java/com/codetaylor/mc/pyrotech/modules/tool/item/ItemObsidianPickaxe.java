package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.ModuleMaterials;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemPickaxe;

public class ItemObsidianPickaxe
    extends ItemPickaxe {

  public static final String NAME = "obsidian_pickaxe";

  public ItemObsidianPickaxe() {

    super(Preconditions.checkNotNull(ModuleMaterials.OBSIDIAN));

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("obsidian");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
