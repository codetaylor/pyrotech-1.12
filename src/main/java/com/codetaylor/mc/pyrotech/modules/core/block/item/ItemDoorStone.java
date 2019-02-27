package com.codetaylor.mc.pyrotech.modules.core.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemDoorStone
    extends ItemDoor {

  public ItemDoorStone(Block block) {

    super(block);
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {

    return 0;
  }
}
