package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileStashStone;
import net.minecraft.tileentity.TileEntity;

public class BlockStashStone
    extends BlockStashBase {

  public static final String NAME = "stash_stone";

  public BlockStashStone() {

    super(1.5f, 10.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileStashStone();
  }
}
