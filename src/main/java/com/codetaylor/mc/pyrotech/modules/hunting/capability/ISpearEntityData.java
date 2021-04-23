package com.codetaylor.mc.pyrotech.modules.hunting.capability;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ISpearEntityData {

  void setSeed(int seed);

  int getSeed();

  void setItemStackCount(int count);

  int getItemStackCount();

  List<ItemStack> getItemStacks(List<ItemStack> result);

  void clearItemStacks();

  void addItemStack(ItemStack itemStack);

}
