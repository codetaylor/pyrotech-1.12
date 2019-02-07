package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockSawmillBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickSawmillTop;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockBrickSawmill
    extends BlockSawmillBase {

  public static final String NAME = "brick_sawmill";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileBrickSawmillTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileBrickSawmill();
  }

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }
}
