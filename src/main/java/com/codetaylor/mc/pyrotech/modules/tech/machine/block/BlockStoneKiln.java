package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockKilnBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneKiln;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneKilnTop;
import net.minecraft.tileentity.TileEntity;

@SuppressWarnings("deprecation")
public class BlockStoneKiln
    extends BlockKilnBase {

  public static final String NAME = "stone_kiln";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileStoneKilnTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileStoneKiln();
  }

}
