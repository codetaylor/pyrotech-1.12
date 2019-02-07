package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockOvenBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickOven;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickOvenTop;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockBrickOven
    extends BlockOvenBase {

  public static final String NAME = "brick_oven";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileBrickOvenTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileBrickOven();
  }

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }
}
