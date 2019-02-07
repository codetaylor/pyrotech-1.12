package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockSawmillBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneSawmillTop;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("deprecation")
public class BlockStoneSawmill
    extends BlockSawmillBase {

  public static final String NAME = "stone_sawmill";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileStoneSawmillTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileStoneSawmill();
  }

}
