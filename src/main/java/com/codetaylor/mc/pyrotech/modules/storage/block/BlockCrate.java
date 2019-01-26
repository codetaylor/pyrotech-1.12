package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockCrateBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileCrate;
import net.minecraft.tileentity.TileEntity;

public class BlockCrate
    extends BlockCrateBase {

  public static final String NAME = "crate";

  public BlockCrate() {

    super(2.0f, 5.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileCrate();
  }
}
