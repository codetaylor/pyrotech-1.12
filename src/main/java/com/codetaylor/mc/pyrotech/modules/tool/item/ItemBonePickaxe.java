package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemPickaxe;

public class ItemBonePickaxe
    extends ItemPickaxe {

  public static final String NAME = "bone_pickaxe";

  public ItemBonePickaxe() {

    super(EnumMaterial.BONE.getToolMaterial());
  }

}
