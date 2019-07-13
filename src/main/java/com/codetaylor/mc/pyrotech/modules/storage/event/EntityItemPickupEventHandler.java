package com.codetaylor.mc.pyrotech.modules.storage.event;

import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockBag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class EntityItemPickupEventHandler {

  private final List<IBagLocator> bagLocatorList;

  public EntityItemPickupEventHandler() {

    this.bagLocatorList = new ArrayList<>(4);

    // Mainhand
    this.bagLocatorList.add((player, result) -> {

      ItemStack heldItemStack = player.getHeldItemMainhand();
      Item heldItem = heldItemStack.getItem();

      if (heldItem instanceof ItemBlockBag
          && ((ItemBlockBag) heldItem).allowAutoPickupMainhand()
          && ((ItemBlockBag) heldItem).isOpen(heldItemStack)) {
        result.add(new BagHandler(heldItemStack));
      }
    });

    // Offhand
    this.bagLocatorList.add((player, result) -> {

      ItemStack heldItemStack = player.getHeldItemOffhand();
      Item heldItem = heldItemStack.getItem();

      if (heldItem instanceof ItemBlockBag
          && ((ItemBlockBag) heldItem).allowAutoPickupOffhand()
          && ((ItemBlockBag) heldItem).isOpen(heldItemStack)) {
        result.add(new BagHandler(heldItemStack));
      }
    });

    // Hotbar
    this.bagLocatorList.add((player, result) -> {

      for (int i = 0; i < 9; i++) {
        ItemStack itemStack = player.inventory.mainInventory.get(i);
        Item item = itemStack.getItem();

        if (item instanceof ItemBlockBag
            && ((ItemBlockBag) item).allowAutoPickupHotbar()
            && ((ItemBlockBag) item).isOpen(itemStack)) {
          result.add(new BagHandler(itemStack));
        }
      }
    });

    // Inventory
    this.bagLocatorList.add((player, result) -> {

      for (int i = 10; i < 36; i++) {
        ItemStack itemStack = player.inventory.mainInventory.get(i);
        Item item = itemStack.getItem();

        if (item instanceof ItemBlockBag
            && ((ItemBlockBag) item).allowAutoPickupInventory()
            && ((ItemBlockBag) item).isOpen(itemStack)) {
          result.add(new BagHandler(itemStack));
        }
      }
    });
  }

  @SubscribeEvent
  public void on(EntityItemPickupEvent event) {

    EntityPlayer entityPlayer = event.getEntityPlayer();

    if (entityPlayer.world.isRemote) {
      return;
    }

    List<BagHandler> handlers = this.locateBags(entityPlayer);

    if (handlers.isEmpty()) {
      return;
    }

    EntityItem entityItem = event.getItem();
    ItemStack itemStack = entityItem.getItem();

    // Check if the item will at least partially fit in a bag

    boolean interested = false;

    for (BagHandler handler : handlers) {

      ItemStack remainingItems = handler.insert(itemStack, true);
      int remainingItemsCount = remainingItems.getCount();

      if (remainingItemsCount != itemStack.getCount()) {
        // stack or partial stack will go into a bag, we're interested
        // in this item
        interested = true;
        break;
      }
    }

    if (!interested) {
      return;
    }

    // Try to fill existing stacks first

    IItemHandler inventory = new PlayerMainInvWrapper(entityPlayer.inventory);

    for (int i = 0; i < inventory.getSlots(); i++) {

      if (!inventory.getStackInSlot(i).isEmpty()) {
        ItemStack remainingItems = inventory.insertItem(i, itemStack, false);

        if (remainingItems.getCount() != itemStack.getCount()) {
          itemStack.setCount(remainingItems.getCount());
        }

        if (remainingItems.getCount() == 0) {
          return;
        }
      }
    }

    for (BagHandler handler : handlers) {

      ItemStack remainingItems = handler.insert(itemStack, false);
      int remainingItemsCount = remainingItems.getCount();

      if (remainingItemsCount != itemStack.getCount()) {
        SoundHelper.playSoundServer(entityPlayer.world, entityPlayer.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.40f, SoundHelper.getPitchEntityItemPickup());
      }

      itemStack.setCount(remainingItemsCount);

      if (remainingItemsCount == 0) {
        break;
      }
    }
  }

  private List<BagHandler> locateBags(EntityPlayer player) {

    List<BagHandler> result = new ArrayList<>(4);

    for (IBagLocator locator : this.bagLocatorList) {
      locator.locateBags(player, result);
    }

    return result;
  }

  private interface IBagLocator {

    void locateBags(EntityPlayer player, List<BagHandler> result);
  }

  private static class BagHandler {

    private final ItemStack bagItemStack;

    /* package */ BagHandler(ItemStack bagItemStack) {

      this.bagItemStack = bagItemStack;
    }

    /* package */ ItemStack insert(ItemStack itemStack, boolean simulate) {

      ItemBlockBag bagItem = (ItemBlockBag) this.bagItemStack.getItem();

      if (bagItem.isItemValidForInsertion(itemStack)) {
        return ItemBlockBag.insertItem(this.bagItemStack, itemStack, simulate);
      }

      return itemStack;
    }
  }

}
