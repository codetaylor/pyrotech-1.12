package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.spi.BlockWorktableBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktableStone;
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

    return ModuleTechBasic.Blocks.WORKTABLE_STONE;
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  protected TileEntity createTileEntity() {

    return new TileWorktableStone();
  }

}
