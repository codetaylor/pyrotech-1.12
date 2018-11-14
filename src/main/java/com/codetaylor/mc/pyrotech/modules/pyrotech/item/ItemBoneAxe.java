package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemAxe;

public class ItemBoneAxe
    extends ItemAxe {

  public static final String NAME = "bone_axe";

  public ItemBoneAxe() {

    super(EnumMaterial.BONE.getToolMaterial(), 8f, -3.2f);
  }
}
