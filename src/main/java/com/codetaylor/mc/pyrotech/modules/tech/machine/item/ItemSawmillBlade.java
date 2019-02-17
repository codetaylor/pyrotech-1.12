package com.codetaylor.mc.pyrotech.modules.tech.machine.item;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSawmillBlade
    extends Item {

  public ItemSawmillBlade(int maxUses) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxUses);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    if (stack.getItem() == ModuleTechMachine.Items.GOLD_MILL_BLADE) {
      return ToolMaterial.GOLD.getEnchantability();
    }

    return super.getItemEnchantability(stack);
  }
}
