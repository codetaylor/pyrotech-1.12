package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockBagBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileBagSimple;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class BlockBagSimple
    extends BlockBagBase {

  public static final String NAME = "bag_simple";

  @Override
  protected TileEntity createTileEntity() {

    return new TileBagSimple();
  }

  @Override
  protected Block getBlock() {

    return ModuleStorage.Blocks.BAG_SIMPLE;
  }

  @Override
  public int getItemCapacity() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.MAX_ITEM_CAPACITY;
  }

  @Override
  protected String[] getItemWhitelist() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ITEM_WHITELIST;
  }

  @Override
  protected String[] getItemBlacklist() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ITEM_BLACKLIST;
  }

  @Override
  public boolean allowAutoPickupMainhand() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOW_AUTO_PICKUP_MAINHAND;
  }

  @Override
  public boolean allowAutoPickupOffhand() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOW_AUTO_PICKUP_OFFHAND;
  }

  @Override
  public boolean allowAutoPickupHotbar() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOW_AUTO_PICKUP_HOTBAR;
  }

  @Override
  public boolean allowAutoPickupInventory() {

    return ModuleStorageConfig.SIMPLE_ROCK_BAG.ALLOW_AUTO_PICKUP_INVENTORY;
  }
}
