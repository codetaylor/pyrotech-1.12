package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockCrucibleBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneCrucibleTop;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("deprecation")
public class BlockStoneCrucible
    extends BlockCrucibleBase {

  public static final String NAME = "stone_crucible";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileStoneCrucibleTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileStoneCrucible();
  }

}
