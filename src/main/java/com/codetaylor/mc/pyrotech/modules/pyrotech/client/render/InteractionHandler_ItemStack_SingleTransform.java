package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Accepts a single transform in the constructor and returns that transform
 * from {@link #getTransform(World, BlockPos, IBlockState, ItemStack)}.
 */
public class InteractionHandler_ItemStack_SingleTransform
    extends InteractionHandler_ItemStack_Base {

  private final Transform transform;

  public InteractionHandler_ItemStack_SingleTransform(ItemStackHandler[] stackHandlers, int slot, EnumFacing[] sides, AxisAlignedBB bounds, Transform transform) {

    super(stackHandlers, slot, sides, bounds);

    this.transform = transform;
  }

  @Override
  public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack) {

    return this.transform;
  }

}
