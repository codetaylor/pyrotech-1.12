package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemHoe;

public class ItemBoneHoe
    extends ItemHoe {

  public static final String NAME = "bone_hoe";

  public ItemBoneHoe() {

    super(EnumMaterial.BONE.getToolMaterial());
  }
}
