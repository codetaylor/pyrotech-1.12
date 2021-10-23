package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemHammerBase;

import java.util.Collections;

public class ItemBoneHammerDurable
    extends ItemHammerBase {

  public static final String NAME = "bone_hammer_durable";

  public ItemBoneHammerDurable() {

    super(EnumMaterial.BONE.getToolMaterial(), Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.BONE_HAMMER_DURABLE_DURABILITY);
  }
}
