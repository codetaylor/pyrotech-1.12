package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileTorchStone;
import net.minecraft.tileentity.TileEntity;

public class BlockTorchStone
    extends BlockTorchBase {

  public static final String NAME = "torch_stone";

  @Override
  public TileEntity createTileEntity() {

    return new TileTorchStone();
  }
}
