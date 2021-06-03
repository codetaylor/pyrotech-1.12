package com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCrucibleBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockCrucibleBase
    extends BlockCombustionWorkerStoneBase {

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCrucibleBase) {
      TileCrucibleBase<?> tile = (TileCrucibleBase<?>) tileEntity;
      FluidTank fluidTank = tile.getOutputFluidTank();
      FluidStack fluid = fluidTank.getFluid();
      int fluidAmount = fluidTank.getFluidAmount();

      if (fluid != null && fluidAmount > 0) {
        int luminosity = fluid.getFluid().getLuminosity(fluid);
        return MathHelper.clamp(luminosity, 0, 15);
      }
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  protected void randomDisplayTickActiveTop(IBlockState state, World world, BlockPos pos, Random rand) {

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
