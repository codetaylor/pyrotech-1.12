package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemHammerBase;

import java.util.Collections;

public class ItemFlintHammerDurable
    extends ItemHammerBase {

  public static final String NAME = "flint_hammer_durable";

  public ItemFlintHammerDurable() {

    super(EnumMaterial.FLINT.getToolMaterial(), Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.FLINT_HAMMER_DURABLE_DURABILITY);
  }
}
