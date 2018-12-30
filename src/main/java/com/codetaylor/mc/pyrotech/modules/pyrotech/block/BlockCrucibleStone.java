package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCrucibleStone;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockCrucibleStone
    extends BlockCombustionWorkerStoneBase {

  public static final String NAME = "crucible_stone";

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileCrucibleStone();
  }

  @Override
  protected void randomDisplayTickWorkingTop(IBlockState state, World world, BlockPos pos, Random rand) {

    double centerX = pos.getX();
    double centerY = pos.getY() - 0.2;
    double centerZ = pos.getZ();

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);

    switch (facing) {

      case NORTH:
        centerX += 8.0 / 16.0;
        centerZ += 18.0 / 16.0;
        break;

      case SOUTH:
        centerX += 8.0 / 16.0;
        centerZ += -2.0 / 16.0;
        break;

      case EAST:
        centerX += -2.0 / 16.0;
        centerZ += 8.0 / 16.0;
        break;

      case WEST:
        centerX += 18.0 / 16.0;
        centerZ += 8.0 / 16.0;
        break;

    }

    world.spawnParticle(
        EnumParticleTypes.SMOKE_LARGE,
        centerX/* + (Util.RANDOM.nextDouble() - 0.5) * 0.25*/,
        centerY,
        centerZ/* + (Util.RANDOM.nextDouble() - 0.5) * 0.25*/,
        0,
        0.05 + (Util.RANDOM.nextFloat() * 2 - 1) * 0.05,
        0
    );
  }
}
