package com.codetaylor.mc.pyrotech.modules.tech.bloomery.event;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;

@GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID)
@Mod.EventBusSubscriber
public class ItemPickupEventHandler {

  @GameRegistry.ObjectHolder(BlockBloom.NAME)
  private static final Item ITEM_BLOOM;

  static {
    ITEM_BLOOM = null;
  }

  @SubscribeEvent
  public static void onEvent(EntityItemPickupEvent event) {

    EntityItem entityItem = event.getItem();
    ItemStack pickedUp = entityItem.getItem();

    if (pickedUp.getItem() != ITEM_BLOOM) {
      return;
    }

    EntityPlayer entityPlayer = event.getEntityPlayer();
    ItemStack heldItem = entityPlayer.getHeldItemMainhand();
    boolean isOffhand = false;

    if (!(heldItem.getItem() instanceof ItemTongsEmptyBase)) {
      heldItem = entityPlayer.getHeldItemOffhand();
      isOffhand = true;
    }

    if (!(heldItem.getItem() instanceof ItemTongsEmptyBase)) {
      return;
    }

    event.getItem().setDead();
    event.setCanceled(true);

    ItemStack itemStack = BloomHelper.createItemTongsFull(heldItem, pickedUp);
    heldItem.shrink(1);

    if (isOffhand) {
      entityPlayer.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemStack);

    } else {
      ItemHandlerHelper.giveItemToPlayer(entityPlayer, itemStack, entityPlayer.inventory.currentItem);
    }
  }

}
