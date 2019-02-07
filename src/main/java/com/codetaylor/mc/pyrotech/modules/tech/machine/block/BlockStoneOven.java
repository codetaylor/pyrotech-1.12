package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockOvenBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneOven;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneOvenTop;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("deprecation")
public class BlockStoneOven
    extends BlockOvenBase {

  public static final String NAME = "stone_oven";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileStoneOvenTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileStoneOven();
  }

}
