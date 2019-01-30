package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class TileWorktableStone
    extends TileWorktable {

  protected int getGridMaxStackSize() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.GRID_MAX_STACK_SIZE;
  }

  protected int getShelfMaxStackSize() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.SHELF_MAX_STACK_SIZE;
  }

  protected int getToolDamagePerCraft() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.TOOL_DAMAGE_PER_CRAFT;
  }

  protected boolean usesDurability() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.USES_DURABILITY;
  }

  protected int getDurability() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.DURABILITY;
  }

  protected int getHitsPerCraft() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.HITS_PER_CRAFT;
  }

  protected int getMinimumHungerToUse() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.MINIMUM_HUNGER_TO_USE;
  }

  protected double getExhaustionCostPerHit() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.EXHAUSTION_COST_PER_HIT;
  }

  protected double getExhaustionCostPerCraftComplete() {

    return ModuleTechBasicConfig.STONE_WORKTABLE.EXHAUSTION_COST_PER_CRAFT_COMPLETE;
  }

  @Override
  protected int getBlockStateIdForParticles() {

    IBlockState state = Blocks.STONE.getDefaultState()
        .withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
    return Block.getStateId(state);
  }
}
