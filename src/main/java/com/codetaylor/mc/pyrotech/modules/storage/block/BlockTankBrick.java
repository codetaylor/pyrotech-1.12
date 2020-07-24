package com.codetaylor.mc.pyrotech.modules.storage.block;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockTankBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileTankBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTankBrick
    extends BlockTankBase {

  public static final String NAME = "brick_tank";

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Override
  protected int getCapacity() {

    return ModuleStorageConfig.BRICK_TANK.CAPACITY;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleStorageConfig.BRICK_TANK.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected boolean canHoldContentsWhenBroken() {

    return ModuleStorageConfig.BRICK_TANK.HOLDS_CONTENTS_WHEN_BROKEN;
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileTankBrick();
  }
}
