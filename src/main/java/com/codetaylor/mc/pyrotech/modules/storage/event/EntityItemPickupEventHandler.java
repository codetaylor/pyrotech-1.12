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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class EntityItemPickupEventHandler {

  private static final List<IBagLocator> BAG_LOCATOR_LIST = new ArrayList<>(3);

  public EntityItemPickupEventHandler() {

    // Mainhand
    BAG_LOCATOR_LIST.add(new IBagLocator() {

      @Nullable
      @Override
      public BagHandler locateBag(EntityPlayer player) {

        ItemStack heldItemStack = player.getHeldItemMainhand();
        Item heldItem = heldItemStack.getItem();

        if (heldItem instanceof ItemBlockBag) {

          if (((ItemBlockBag) heldItem).allowAutoPickupMainhand()) {
            return new BagHandler(heldItemStack);
          }
        }

        return null;
      }
    });

    // Offhand
    BAG_LOCATOR_LIST.add(new IBagLocator() {

      @Nullable
      @Override
      public BagHandler locateBag(EntityPlayer player) {

        ItemStack heldItemStack = player.getHeldItemOffhand();
        Item heldItem = heldItemStack.getItem();

        if (heldItem instanceof ItemBlockBag) {

          if (((ItemBlockBag) heldItem).allowAutoPickupOffhand()) {
            return new BagHandler(heldItemStack);
          }
        }

        return null;
      }
    });

    // Hotbar
    BAG_LOCATOR_LIST.add(new IBagLocator() {

      @Nullable
      @Override
      public BagHandler locateBag(EntityPlayer player) {

        for (int i = 0; i < 9; i++) {
          ItemStack itemStack = player.inventory.mainInventory.get(i);
          Item item = itemStack.getItem();

          if (item instanceof ItemBlockBag) {

            if (((ItemBlockBag) item).allowAutoPickupHotbar()) {
              return new BagHandler(itemStack);
            }
          }
        }

        return null;
      }
    });

    // Inventory
    BAG_LOCATOR_LIST.add(new IBagLocator() {

      @Nullable
      @Override
      public BagHandler locateBag(EntityPlayer player) {

        for (int i = 10; i < 36; i++) {
          ItemStack itemStack = player.inventory.mainInventory.get(i);
          Item item = itemStack.getItem();

          if (item instanceof ItemBlockBag) {

            if (((ItemBlockBag) item).allowAutoPickupInventory()) {
              return new BagHandler(itemStack);
            }
          }
        }

        return null;
      }
    });
  }

  @SubscribeEvent
  public void on(EntityItemPickupEvent event) {

    EntityPlayer entityPlayer = event.getEntityPlayer();

    if (entityPlayer.world.isRemote) {
      return;
    }

    BagHandler handler = this.locateBag(entityPlayer);

    if (handler == null) {
      return;
    }

    EntityItem entityItem = event.getItem();
    ItemStack itemStack = entityItem.getItem();
    ItemStack remainingItems = handler.insert(itemStack);

    if (remainingItems.getCount() != itemStack.getCount()) {
      SoundHelper.playSoundServer(entityPlayer.world, entityPlayer.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.40f, SoundHelper.getPitchEntityItemPickup());
    }

    itemStack.setCount(remainingItems.getCount());
  }

  private BagHandler locateBag(EntityPlayer player) {

    BagHandler handler;

    for (IBagLocator locator : BAG_LOCATOR_LIST) {

      if ((handler = locator.locateBag(player)) != null) {
        return handler;
      }
    }

    return null;
  }

  private interface IBagLocator {

    @Nullable
    BagHandler locateBag(EntityPlayer player);
  }

  private static class BagHandler {

    private final ItemStack bagItemStack;

    /* package */ BagHandler(ItemStack bagItemStack) {

      this.bagItemStack = bagItemStack;
    }

    /* package */ ItemStack insert(ItemStack itemStack) {

      ItemBlockBag bagItem = (ItemBlockBag) this.bagItemStack.getItem();

      if (bagItem.isItemValidForInsertion(itemStack)) {
        TileBagBase.StackHandler handler = (TileBagBase.StackHandler) this.bagItemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (handler != null) {
          ItemStack remainingItems = handler.insertItem(itemStack.copy(), false);

          if (itemStack.getCount() != remainingItems.getCount()) {
            bagItem.updateCount(this.bagItemStack);
          }

          return remainingItems;
        }
      }

      return itemStack;
    }
  }

}
