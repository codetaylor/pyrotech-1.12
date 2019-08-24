package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockFaucetBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileFaucetStone;
import net.minecraft.tileentity.TileEntity;

public class BlockFaucetStone
    extends BlockFaucetBase {

  public static final String NAME = "faucet_stone";

  @Override
  protected boolean canTransferHotFluids() {

    return ModuleStorageConfig.STONE_FAUCET.TRANSFERS_HOT_FLUIDS;
  }

  @Override
  protected int getTransferLimit() {

    return ModuleStorageConfig.STONE_FAUCET.TRANSFER_LIMIT;
  }

  @Override
  protected int getTransferAmountPerTick() {

    return ModuleStorageConfig.STONE_FAUCET.TRANSFER_AMOUNT_PER_TICK;
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileFaucetStone();
  }
}
