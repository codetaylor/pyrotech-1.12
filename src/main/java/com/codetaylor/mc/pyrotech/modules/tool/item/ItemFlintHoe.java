package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemHoe;

public class ItemFlintHoe
    extends ItemHoe {

  public static final String NAME = "flint_hoe";

  public ItemFlintHoe() {

    super(EnumMaterial.FLINT.getToolMaterial());
  }

}
