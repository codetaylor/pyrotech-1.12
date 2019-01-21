package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemTongs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber
public class ItemPickupEventHandler {

  @SubscribeEvent
  public static void onEvent(EntityItemPickupEvent event) {

    EntityItem entityItem = event.getItem();
    ItemStack pickedUp = entityItem.getItem();

    if (pickedUp.getItem() != Item.getItemFromBlock(ModuleBlocks.BLOOM)) {
      return;
    }

    EntityPlayer entityPlayer = event.getEntityPlayer();
    ItemStack heldItem = entityPlayer.getHeldItemMainhand();
    boolean isOffhand = false;

    if (heldItem.getItem() != ModuleItems.TONGS) {
      heldItem = entityPlayer.getHeldItemOffhand();
      isOffhand = true;
    }

    if (heldItem.getItem() != ModuleItems.TONGS) {
      return;
    }

    event.getItem().setDead();
    event.setCanceled(true);

    ItemStack itemStack = ItemTongs.getFilledItemStack(heldItem, pickedUp);
    heldItem.shrink(1);

    if (isOffhand) {
      entityPlayer.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemStack);

    } else {
      ItemHandlerHelper.giveItemToPlayer(entityPlayer, itemStack, entityPlayer.inventory.currentItem);
    }
  }

}
