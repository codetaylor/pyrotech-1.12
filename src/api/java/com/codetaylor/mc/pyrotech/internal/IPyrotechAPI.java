package com.codetaylor.mc.pyrotech.internal;

import net.minecraft.item.Item;

public interface IPyrotechAPI {

  default void registerHammer(Item item, int harvestLevel) {
    // no-op
  }

}
