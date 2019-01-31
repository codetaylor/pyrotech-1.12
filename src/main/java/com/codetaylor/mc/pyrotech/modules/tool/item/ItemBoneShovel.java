package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemSpade;

public class ItemBoneShovel
    extends ItemSpade {

  public static final String NAME = "bone_shovel";

  public ItemBoneShovel() {

    super(EnumMaterial.BONE.getToolMaterial());
  }

}
