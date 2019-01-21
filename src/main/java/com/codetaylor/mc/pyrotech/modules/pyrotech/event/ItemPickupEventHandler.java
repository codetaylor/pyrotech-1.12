package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
    EntityPlayer entityPlayer = event.getEntityPlayer();
    ItemStack heldItem = entityPlayer.getHeldItemMainhand();

    if (heldItem.getItem() == ModuleItems.TONGS
        && pickedUp.getItem() == Item.getItemFromBlock(ModuleBlocks.BLOOM)) {
      ItemStack itemStack = new ItemStack(ModuleItems.TONGS_FULL, 1, heldItem.getMetadata());
      itemStack.setTagCompound(pickedUp.getTagCompound());
      heldItem.shrink(1);
      ItemHandlerHelper.giveItemToPlayer(entityPlayer, itemStack);
      event.getItem().setDead();
      event.setCanceled(true);
    }
  }

}
