package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileShelfStone;
import net.minecraft.tileentity.TileEntity;

public class BlockShelfStone
    extends BlockShelfBase {

  public static final String NAME = "shelf_stone";

  public BlockShelfStone() {

    super(1.5f, 10.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileShelfStone();
  }
}
