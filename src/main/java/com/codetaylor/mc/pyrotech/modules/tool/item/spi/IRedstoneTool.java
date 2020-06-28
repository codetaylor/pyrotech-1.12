package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import net.minecraft.item.ItemStack;

public interface IRedstoneTool {

  void activateRedstoneTool(ItemStack itemStack);

  boolean isRedstoneToolActive(ItemStack itemStack);

  int getRedstoneToolDamage(ItemStack itemStack);

  /**
   * This method must delegate to the super setDamage method.
   */
  void setRedstoneToolDamage(ItemStack itemStack, int damage);
}
