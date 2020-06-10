package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;

import javax.annotation.Nullable;

public class TileStashStone
    extends TileStash {

  @Override
  protected int getMaxStacks() {

    return ModuleStorageConfig.DURABLE_STASH.MAX_STACKS;
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleStorageConfig.DURABLE_STASH.ALLOW_AUTOMATION;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleStorageConfig.STAGES_DURABLE_STASH;
  }
}
