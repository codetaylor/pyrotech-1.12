package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemHammerBase;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class ItemGoldHammer
    extends ItemHammerBase {

  public static final String NAME = "gold_hammer";

  public ItemGoldHammer() {

    super(ToolMaterial.GOLD, Collections.emptySet());
    this.setMaxDamage(ModuleCoreConfig.HAMMERS.GOLD_HAMMER_DURABILITY);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolMaterial.GOLD.getEnchantability();
  }
}
