package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockBagBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileBagDurable;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileBagSimple;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class BlockBagDurable
    extends BlockBagBase {

  public static final String NAME = "bag_durable";

  @Override
  protected TileEntity createTileEntity() {

    return new TileBagDurable();
  }

  @Override
  protected Block getBlock() {

    return ModuleStorage.Blocks.BAG_DURABLE;
  }

  @Override
  public int getItemCapacity() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.MAX_ITEM_CAPACITY;
  }

  @Override
  protected String[] getItemWhitelist() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.ITEM_WHITELIST;
  }

  @Override
  protected String[] getItemBlacklist() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.ITEM_BLACKLIST;
  }
  @Override
  public boolean allowAutoPickupMainhand() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.ALLOW_AUTO_PICKUP_MAINHAND;
  }

  @Override
  public boolean allowAutoPickupOffhand() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.ALLOW_AUTO_PICKUP_OFFHAND;
  }

  @Override
  public boolean allowAutoPickupHotbar() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.ALLOW_AUTO_PICKUP_HOTBAR;
  }

  @Override
  public boolean allowAutoPickupInventory() {

    return ModuleStorageConfig.DURABLE_ROCK_BAG.ALLOW_AUTO_PICKUP_INVENTORY;
  }

}
