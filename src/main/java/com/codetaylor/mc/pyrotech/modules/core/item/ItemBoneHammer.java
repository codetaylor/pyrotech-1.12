package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemBoneHammer
    extends ItemTool {

  public static final String NAME = "bone_hammer";

  public ItemBoneHammer() {

    super(EnumMaterial.BONE.getToolMaterial(), Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.BONE_HAMMER_DURABILITY);
  }
}
