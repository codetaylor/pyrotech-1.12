package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemSword;

public class ItemFlintSword
    extends ItemSword {

  public static final String NAME = "flint_sword";

  public ItemFlintSword() {

    super(EnumMaterial.FLINT.getToolMaterial());
  }
}
