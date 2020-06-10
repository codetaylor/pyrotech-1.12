package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileFaucetBase;

import javax.annotation.Nullable;

public class TileFaucetStone
    extends TileFaucetBase {

  @Override
  protected boolean canTransferHotFluids() {

    return ModuleStorageConfig.STONE_FAUCET.TRANSFERS_HOT_FLUIDS;
  }

  @Override
  protected int getHotFluidTemperature() {

    return ModuleStorageConfig.STONE_FAUCET.HOT_TEMPERATURE;
  }

  @Override
  protected int getFluidTransferMBPerTick() {

    return ModuleStorageConfig.STONE_FAUCET.TRANSFER_AMOUNT_PER_TICK;
  }

  @Override
  protected int getFluidTransferLimit() {

    return ModuleStorageConfig.STONE_FAUCET.TRANSFER_LIMIT;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleStorageConfig.STAGES_FAUCET_STONE;
  }
}
