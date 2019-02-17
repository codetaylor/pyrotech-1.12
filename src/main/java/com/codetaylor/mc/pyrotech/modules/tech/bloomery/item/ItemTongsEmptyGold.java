package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;
import net.minecraft.item.ItemStack;

public class ItemTongsEmptyGold
    extends ItemTongsEmptyBase {

  public static final String NAME = "tongs_gold";

  public ItemTongsEmptyGold() {

    super(() -> ModuleTechBloomery.Items.TONGS_GOLD_FULL, ModuleTechBloomeryConfig.TONGS.GOLD_TONGS_DURABILITY);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolMaterial.GOLD.getEnchantability();
  }
}
