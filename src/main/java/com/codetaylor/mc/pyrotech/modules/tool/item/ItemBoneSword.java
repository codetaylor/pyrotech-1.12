package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemSword;

public class ItemBoneSword
    extends ItemSword {

  public static final String NAME = "bone_sword";

  public ItemBoneSword() {

    super(EnumMaterial.BONE.getToolMaterial());
  }
}
