package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IInteractionRenderer<I extends IInteraction> {

  void renderSolidPass(I interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks);

  /**
   * @param interaction
   * @param world
   * @param renderItem
   * @param hitSide
   * @param hitVec
   * @param hitPos
   * @param blockState
   * @param heldItemMainHand
   * @param partialTicks
   * @return true if any rendering was done
   */
  boolean renderAdditivePass(I interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks);

}
