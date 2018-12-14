package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemFlintHammer
    extends ItemTool {

  public static final String NAME = "flint_hammer";

  public ItemFlintHammer() {

    super(EnumMaterial.FLINT.getToolMaterial(), Collections.emptySet());
  }
}
