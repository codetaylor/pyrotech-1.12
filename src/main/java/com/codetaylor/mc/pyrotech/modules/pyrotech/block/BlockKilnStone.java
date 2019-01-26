package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockKilnStone
    extends BlockCombustionWorkerStoneBase {

  public static final String NAME = "kiln_stone";

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileKilnStone();
  }

  @Override
  protected void randomDisplayTickWorkingTop(IBlockState state, World world, BlockPos pos, Random rand) {

    double centerX = pos.getX() + 0.5;
    double centerY = pos.getY() + 0.25;
    double centerZ = pos.getZ() + 0.5;

    world.spawnParticle(
        EnumParticleTypes.SMOKE_LARGE,
        centerX + (Util.RANDOM.nextDouble() - 0.5),
        centerY,
        centerZ + (Util.RANDOM.nextDouble() - 0.5),
        0,
        0.05 + (Util.RANDOM.nextFloat() * 2 - 1) * 0.05,
        0
    );
  }
}
