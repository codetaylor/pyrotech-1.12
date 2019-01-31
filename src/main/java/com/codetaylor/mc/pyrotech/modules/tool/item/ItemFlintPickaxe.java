package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemPickaxe;

public class ItemFlintPickaxe
    extends ItemPickaxe {

  public static final String NAME = "flint_pickaxe";

  public ItemFlintPickaxe() {

    super(EnumMaterial.FLINT.getToolMaterial());
  }

}
