package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class ItemBlockCarcass
    extends ItemBlock {

  public ItemBlockCarcass(Block block) {

    super(block);
    this.setMaxStackSize(1);
  }

  public static ItemStackHandler getItemStackHandler(ItemStack itemStack) {

    NBTTagCompound tag = itemStack.getTagCompound();

    if (tag == null || !tag.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {
      return null;
    }

    NBTTagCompound tileTag = tag.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);

    if (!tileTag.hasKey("inputStackHandler")) {
      return null;
    }

    ItemStackHandler stackHandler = new ItemStackHandler();
    stackHandler.deserializeNBT(tileTag.getCompoundTag("inputStackHandler"));
    return stackHandler;
  }

  public static void updateItemStackHandler(ItemStack itemStack, ItemStackHandler stackHandler) {

    NBTTagCompound tag = itemStack.getTagCompound();

    if (tag == null || !tag.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {
      return;
    }

    NBTTagCompound tileTag = tag.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
    tileTag.setTag("inputStackHandler", stackHandler.serializeNBT());
    itemStack.setTagCompound(tag);
  }
}
