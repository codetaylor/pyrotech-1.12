package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInteractionItemStack<T extends TileEntity & ITileInteractable>
    extends IInteraction<T> {

  /**
   * Returns true if the given {@link ItemStack} is allowed to be inserted.
   * <p>
   * <p>
   * This is used as a check before attempting to insert an item, therefore,
   * this method should only be concerned with the type of item and not
   * whether the item's quantity will fit into the slot.
   * <p>
   * <p>
   * This method is used to prevent rendering ghost items.
   * This method will be called every frame when a player is looking at an
   * interaction handler and, as such, should be fast. Take care to cache and
   * optimize accordingly.
   *
   * @param itemStack the {@link ItemStack} to insert
   * @return true if the given {@link ItemStack} is allowed to be inserted
   */
  default boolean isItemStackValid(ItemStack itemStack) {

    return true;
  }

  /**
   * @return true if this interaction handler is empty
   */
  boolean isEmpty();

  /**
   * @return the stack in this interaction
   */
  ItemStack getStackInSlot();

  /**
   * Extract the given amount from this interaction.
   *
   * @param amount   the amount to extract
   * @param simulate true to only simulate the extraction
   * @return a stack with the extracted items
   */
  ItemStack extract(int amount, boolean simulate);

  /**
   * Insert the given stack into this interaction.
   *
   * @param itemStack the stack to insert
   * @param simulate  true to only simulate the insertion
   * @return a stack with the remaining items that weren't inserted, or an empty stack
   */
  ItemStack insert(ItemStack itemStack, boolean simulate);

  /**
   * Returns the transform used to render an item in this slot.
   * <p>
   * Different transforms can be returned based on item, blockState, etc.
   *
   * @param world        the world
   * @param pos          the pos of the parent
   * @param blockState   the blockState of the parent
   * @param itemStack    the itemStack to render
   * @param partialTicks value passed from the TESR
   * @return the transform used to render an item in this slot
   */
  Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, float partialTicks);
}
