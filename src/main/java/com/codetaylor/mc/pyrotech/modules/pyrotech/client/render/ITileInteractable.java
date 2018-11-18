package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileInteractable {

  IInteractionHandler[] EMPTY_INTERACTION_HANDLERS = new IInteractionHandler[0];
  IInteractionHandlerItemStack[] EMPTY_ITEMSTACK_INTERACTION_HANDLERS = new IInteractionHandlerItemStack[0];

  default IInteractionHandler[] getInteractionHandlers() {

    return EMPTY_INTERACTION_HANDLERS;
  }

  default IInteractionHandlerItemStack[] getItemStackInteractionHandlers() {

    return EMPTY_ITEMSTACK_INTERACTION_HANDLERS;
  }

  /**
   * Return true if the given position is a valid interaction position.
   * <p>
   * Use this to extend a tile entity's interaction beyond one block.
   * <p>
   * This is called every frame, optimize accordingly.
   *
   * @param world      the world
   * @param pos        the pos to check
   * @param blockState the blockState at the given pos
   * @return true if the given position is a valid interaction position
   */
  default boolean isExtendedInteraction(World world, BlockPos pos, IBlockState blockState) {

    return false;
  }
}
