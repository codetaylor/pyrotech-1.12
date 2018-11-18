package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInteractionHandlerItemStack
    extends IInteractionHandler {

  boolean isItemStackValid(ItemStack itemStack);

  /**
   * @return true if this interaction handler is empty
   */
  boolean isEmpty();

  ItemStack getStackInSlot();

  ItemStack extract(int amount, boolean simulate);

  ItemStack insert(ItemStack itemStack, boolean simulate);

  /**
   * Returns the transforms used to render an item in this slot.
   * <p>
   * Different transforms can be returned based on item, blockState, etc.
   *
   * @param world      the world
   * @param pos        the pos of the parent
   * @param blockState the blockState of the parent
   * @param itemStack  the itemStack to render
   * @return the transforms used to render an item in this slot
   */
  Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack);
}
