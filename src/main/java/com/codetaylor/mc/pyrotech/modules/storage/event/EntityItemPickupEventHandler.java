package com.codetaylor.mc.pyrotech.modules.storage.event;

import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockBag;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

@Mod.EventBusSubscriber
public class EntityItemPickupEventHandler {

  @SubscribeEvent
  public static void on(EntityItemPickupEvent event) {

    EntityPlayer entityPlayer = event.getEntityPlayer();

    if (entityPlayer.world.isRemote) {
      return;
    }

    ItemStack bagItemStack = EntityItemPickupEventHandler.locateBag(entityPlayer);

    if (!bagItemStack.isEmpty()) {
      EntityItem entityItem = event.getItem();
      ItemStack item = entityItem.getItem();
      ItemBlockBag bagItem = (ItemBlockBag) bagItemStack.getItem();

      if (bagItem.isItemValidForInsertion(item)) {
        TileBagBase.StackHandler handler = (TileBagBase.StackHandler) bagItemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (handler != null) {
          ItemStack remainingItems = handler.insertItem(item.copy(), false);
          item.setCount(remainingItems.getCount());

          if (remainingItems.getCount() == 0) {
            SoundHelper.playSoundServer(entityPlayer.world, entityPlayer.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.40f, SoundHelper.getPitchEntityItemPickup());
          }
        }
      }
    }
  }

  private static ItemStack locateBag(EntityPlayer entityPlayer) {

    ItemStack heldItemOffhand = entityPlayer.getHeldItemOffhand();
    Item heldItemOffhandItem = heldItemOffhand.getItem();

    if (heldItemOffhandItem instanceof ItemBlockBag
        && ((ItemBlockBag) heldItemOffhandItem).isOpen(heldItemOffhand)) {
      return heldItemOffhand;
    }

    for (int i = 0; i < 9; i++) {
      ItemStack itemStack = entityPlayer.inventory.mainInventory.get(i);
      Item item = itemStack.getItem();

      if (item instanceof ItemBlockBag
          && ((ItemBlockBag) item).isOpen(itemStack)) {
        return itemStack;
      }
    }

    return ItemStack.EMPTY;
  }

}
