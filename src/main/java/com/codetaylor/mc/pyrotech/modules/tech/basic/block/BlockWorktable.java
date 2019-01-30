package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.spi.BlockWorktableBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockWorktable
    extends BlockWorktableBase
    implements IBlockInteractable {

  public static final String NAME = "worktable";

  public BlockWorktable() {

    super(Material.WOOD, 2.0f, 5.0f);
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  protected Block getDroppedBlock() {

    return ModuleTechBasic.Blocks.WORKTABLE;
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  protected TileEntity createTileEntity() {

    return new TileWorktable();
  }

}
