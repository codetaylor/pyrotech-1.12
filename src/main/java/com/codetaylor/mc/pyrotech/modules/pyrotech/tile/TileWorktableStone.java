package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

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

}
