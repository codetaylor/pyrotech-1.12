package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockCrucibleBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickCrucibleTop;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockBrickCrucible
    extends BlockCrucibleBase {

  public static final String NAME = "brick_crucible";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileBrickCrucibleTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileBrickCrucible();
  }

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }
}
