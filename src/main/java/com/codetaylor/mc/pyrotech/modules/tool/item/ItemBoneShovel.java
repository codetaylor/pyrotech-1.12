package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemSpade;

public class ItemBoneShovel
    extends ItemSpade {

  public static final String NAME = "bone_shovel";

  public ItemBoneShovel() {

    super(EnumMaterial.BONE.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("bone");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
