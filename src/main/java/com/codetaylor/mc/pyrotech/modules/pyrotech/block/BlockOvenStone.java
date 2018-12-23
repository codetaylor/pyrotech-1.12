package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnStone;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileOvenStone;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("deprecation")
public class BlockOvenStone
    extends BlockCombustionWorkerStoneBase {

  public static final String NAME = "oven_stone";

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileOvenStone();
  }
}
