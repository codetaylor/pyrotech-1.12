package com.codetaylor.mc.pyrotech.modules.bucket.event;

import com.codetaylor.mc.pyrotech.modules.bucket.item.ItemBucketBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class ItemCraftedEventHandler {

  @SubscribeEvent
  public static void on(PlayerEvent.ItemCraftedEvent craftedEvent) {

    int size = craftedEvent.craftMatrix.getSizeInventory();

    for (int slot = 0; slot < size; slot++) {
      ItemStack itemStack = craftedEvent.craftMatrix.getStackInSlot(slot);
      Item item = itemStack.getItem();

      if (item instanceof ItemBucketBase) {
        ItemBucketBase bucket = (ItemBucketBase) item;
        ItemStack copy = itemStack.copy();

        int durability = bucket.getDurability(copy);
        bucket.setDurability(copy, durability - 1);
        craftedEvent.craftMatrix.setInventorySlotContents(slot, copy);
      }
    }
  }
}
