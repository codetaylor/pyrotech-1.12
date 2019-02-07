package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockKilnBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickKiln;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickKilnTop;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockBrickKiln
    extends BlockKilnBase {

  public static final String NAME = "brick_kiln";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileBrickKilnTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileBrickKiln();
  }

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }
}
