package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.item.ItemAxe;

public class ItemBoneAxe
    extends ItemAxe {

  public static final String NAME = "bone_axe";

  public ItemBoneAxe() {

    super(EnumMaterial.BONE.getToolMaterial(), 8f, -3.2f);

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("bone");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
