package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
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
}
