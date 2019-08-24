package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockFaucetBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileFaucetBrick;
import net.minecraft.tileentity.TileEntity;

public class BlockFaucetBrick
    extends BlockFaucetBase {

  public static final String NAME = "faucet_brick";

  @Override
  protected boolean canTransferHotFluids() {

    return ModuleStorageConfig.BRICK_FAUCET.TRANSFERS_HOT_FLUIDS;
  }

  @Override
  protected int getTransferLimit() {

    return ModuleStorageConfig.BRICK_FAUCET.TRANSFER_LIMIT;
  }

  @Override
  protected int getTransferAmountPerTick() {

    return ModuleStorageConfig.BRICK_FAUCET.TRANSFER_AMOUNT_PER_TICK;
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileFaucetBrick();
  }
}
