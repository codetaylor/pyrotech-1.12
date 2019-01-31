package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemSpade;

public class ItemFlintShovel
    extends ItemSpade {

  public static final String NAME = "flint_shovel";

  public ItemFlintShovel() {

    super(EnumMaterial.FLINT.getToolMaterial());
  }

}
