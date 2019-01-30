package com.codetaylor.mc.pyrotech.library.spi.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockIgnitableAdjacentFire {

  /**
   * Called when a block is adjacent to fire.
   *
   * @param world      the world
   * @param pos        the pos of the block
   * @param blockState the blockState of the block
   * @param facing     the direction of the adjacent fire
   */
  void igniteWithAdjacentFire(World world, BlockPos pos, IBlockState blockState, EnumFacing facing);

}
