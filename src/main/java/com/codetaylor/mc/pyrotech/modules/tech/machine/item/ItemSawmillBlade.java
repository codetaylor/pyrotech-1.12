package com.codetaylor.mc.pyrotech.modules.tech.machine.item;

import net.minecraft.item.Item;

public class ItemSawmillBlade
    extends Item {

  public ItemSawmillBlade(int maxUses) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxUses);
  }
}
