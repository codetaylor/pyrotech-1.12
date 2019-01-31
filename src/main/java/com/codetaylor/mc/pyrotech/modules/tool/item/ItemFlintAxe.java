package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemAxe;

public class ItemFlintAxe
    extends ItemAxe {

  public static final String NAME = "flint_axe";

  public ItemFlintAxe() {

    super(EnumMaterial.FLINT.getToolMaterial(), 8f, -3.2f);
  }

}
