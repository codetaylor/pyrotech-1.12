package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IInteractionRenderer<I extends IInteraction> {

  IInteractionRenderer<IInteraction> NO_OP = new NoOp();

  void renderAdditivePass(IInteractionItemStack interaction, World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos pos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks);

  void renderSolidPass(IInteractionItemStack interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks);

  class NoOp
      implements IInteractionRenderer<IInteraction> {

    @Override
    public void renderSolidPass(IInteractionItemStack interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {
      // no op
    }

    @Override
    public void renderAdditivePass(IInteractionItemStack interaction, World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos pos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {
      // no op
    }
  }

}
