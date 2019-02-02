package com.codetaylor.mc.pyrotech.modules.tech.machine.item;

import net.minecraft.item.Item;

public class ItemCog
    extends Item {

  public ItemCog(int maxUses) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxUses);
  }
}
