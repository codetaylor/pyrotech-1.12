package com.codetaylor.mc.pyrotech.library.spi.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockIgnitableWithIgniterItem {

  void igniteWithIgniterItem(World world, BlockPos pos, IBlockState blockState, EnumFacing facing);

}
