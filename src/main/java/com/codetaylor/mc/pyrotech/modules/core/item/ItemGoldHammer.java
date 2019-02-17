package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Collections;

public class ItemGoldHammer
    extends ItemTool {

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
