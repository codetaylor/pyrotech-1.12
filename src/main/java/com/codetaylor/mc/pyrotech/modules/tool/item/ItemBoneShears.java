package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;

public class ItemBoneShears
    extends ItemShearsBase {

  public static final String NAME = "bone_shears";

  public ItemBoneShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("bone"));
  }
}
