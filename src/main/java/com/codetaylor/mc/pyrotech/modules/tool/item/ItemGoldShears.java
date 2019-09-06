package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;
import net.minecraft.item.ItemStack;

public class ItemGoldShears
    extends ItemShearsBase {

  public static final String NAME = "gold_shears";

  public ItemGoldShears() {

    super("gold");
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolMaterial.GOLD.getEnchantability();
  }
}
