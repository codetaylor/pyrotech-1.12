package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.block.BlockCraftingTableTemplate;
import net.minecraft.item.ItemBlock;

public class ItemCraftingTableTemplate
    extends ItemBlock {

  public ItemCraftingTableTemplate(BlockCraftingTableTemplate block) {

    super(block);
    this.setMaxStackSize(1);
  }
}
