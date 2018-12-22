package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnStone;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("deprecation")
public class BlockKilnStone
    extends BlockCombustionWorkerStoneBase {

  public static final String NAME = "kiln_stone";

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileKilnStone();
  }
}
