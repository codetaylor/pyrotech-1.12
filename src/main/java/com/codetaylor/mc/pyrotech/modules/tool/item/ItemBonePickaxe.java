package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemPickaxeBase;

public class ItemBonePickaxe
    extends ItemPickaxeBase {

  public static final String NAME = "bone_pickaxe";

  public ItemBonePickaxe() {

    super(EnumMaterial.BONE.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("bone");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

}
