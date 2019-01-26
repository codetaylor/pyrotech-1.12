package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockCrateBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileCrateStone;
import net.minecraft.tileentity.TileEntity;

public class BlockCrateStone
    extends BlockCrateBase {

  public static final String NAME = "crate_stone";

  public BlockCrateStone() {

    super(1.5f, 10.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileCrateStone();
  }
}
