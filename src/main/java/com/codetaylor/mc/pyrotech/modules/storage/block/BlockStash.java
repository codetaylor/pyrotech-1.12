package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockStashBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
import net.minecraft.tileentity.TileEntity;

public class BlockStash
    extends BlockStashBase {

  public static final String NAME = "stash";

  public BlockStash() {

    super(2.0f, 5.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileStash();
  }
}
