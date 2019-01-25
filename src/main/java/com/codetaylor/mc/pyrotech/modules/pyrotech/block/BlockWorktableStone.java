package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockWorktableBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileWorktableStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockWorktableStone
    extends BlockWorktableBase
    implements IBlockInteractable {

  public static final String NAME = "worktable_stone";

  public BlockWorktableStone() {

    super(Material.WOOD, 1.5f, 10.0f);
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  protected Block getDroppedBlock() {

    return ModuleBlocks.WORKTABLE_STONE;
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  protected TileEntity createTileEntity() {

    return new TileWorktableStone();
  }

}
