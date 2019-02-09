package com.codetaylor.mc.pyrotech.modules.tool.item;

import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemUnfiredClayShears
    extends ItemShears {

  public static final String NAME = "unfired_clay_shears";

  public ItemUnfiredClayShears() {

    this.setMaxStackSize(1);
  }

  @Override
  public boolean isEnchantable(@Nonnull ItemStack stack) {

    return false;
  }
}
