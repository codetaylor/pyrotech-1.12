package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockShelfBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileShelf;
import net.minecraft.tileentity.TileEntity;

public class BlockShelf
    extends BlockShelfBase {

  public static final String NAME = "shelf";

  public BlockShelf() {

    super(2.0f, 5.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileShelf();
  }
}
