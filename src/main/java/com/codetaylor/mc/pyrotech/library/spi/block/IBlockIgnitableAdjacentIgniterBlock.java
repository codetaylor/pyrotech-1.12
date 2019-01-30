package com.codetaylor.mc.pyrotech.library.spi.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockIgnitableAdjacentIgniterBlock {

  /**
   * Called when a block is adjacent to an active igniter block.
   *
   * @param world      the world
   * @param pos        the pos of the block
   * @param blockState the blockState of the block
   * @param facing     the direction of the adjacent igniter block
   */
  void igniteWithAdjacentIgniterBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing);

}
