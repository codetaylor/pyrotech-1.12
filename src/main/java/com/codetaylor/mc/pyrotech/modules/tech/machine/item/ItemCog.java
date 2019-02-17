package com.codetaylor.mc.pyrotech.modules.tech.machine.item;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCog
    extends Item {

  private int burnTime;

  public ItemCog(int maxUses) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxUses);
    this.burnTime = 0;
  }

  public Item setBurnTime(int burnTime) {

    this.burnTime = burnTime;
    return this;
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {

    return this.burnTime;
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    if (stack.getItem() == ModuleTechMachine.Items.GOLD_COG) {
      return ToolMaterial.GOLD.getEnchantability();
    }

    return super.getItemEnchantability(stack);
  }
}
