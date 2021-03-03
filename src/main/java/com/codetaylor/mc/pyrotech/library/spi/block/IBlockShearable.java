package com.codetaylor.mc.pyrotech.library.spi.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockShearable {

  void onSheared(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, EntityPlayer player);
}
