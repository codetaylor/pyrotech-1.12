package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.ModuleMaterials;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemSpade;

public class ItemObsidianShovel
    extends ItemSpade {

  public static final String NAME = "obsidian_shovel";

  public ItemObsidianShovel() {

    super(Preconditions.checkNotNull(ModuleMaterials.OBSIDIAN));

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("obsidian");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
