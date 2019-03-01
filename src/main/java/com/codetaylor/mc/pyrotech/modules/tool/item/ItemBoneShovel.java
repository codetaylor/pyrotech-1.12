package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShovelBase;

public class ItemBoneShovel
    extends ItemShovelBase {

  public static final String NAME = "bone_shovel";

  public ItemBoneShovel() {

    super(EnumMaterial.BONE.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("bone");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }
}
