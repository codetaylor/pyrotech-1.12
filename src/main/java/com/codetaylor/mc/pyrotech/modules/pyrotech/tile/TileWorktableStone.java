package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class TileWorktableStone
    extends TileWorktable {

  protected int getGridMaxStackSize() {

    return ModulePyrotechConfig.STONE_WORKTABLE.GRID_MAX_STACK_SIZE;
  }

  protected int getShelfMaxStackSize() {

    return ModulePyrotechConfig.STONE_WORKTABLE.SHELF_MAX_STACK_SIZE;
  }

  protected int getToolDamagePerCraft() {

    return ModulePyrotechConfig.STONE_WORKTABLE.TOOL_DAMAGE_PER_CRAFT;
  }

  protected boolean usesDurability() {

    return ModulePyrotechConfig.STONE_WORKTABLE.USES_DURABILITY;
  }

  protected int getDurability() {

    return ModulePyrotechConfig.STONE_WORKTABLE.DURABILITY;
  }

  protected int getHitsPerCraft() {

    return ModulePyrotechConfig.STONE_WORKTABLE.HITS_PER_CRAFT;
  }

  @Override
  protected int getBlockStateIdForParticles() {

    IBlockState state = Blocks.STONE.getDefaultState()
        .withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
    return Block.getStateId(state);
  }
}
