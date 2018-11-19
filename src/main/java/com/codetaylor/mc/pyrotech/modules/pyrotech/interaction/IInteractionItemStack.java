package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInteractionItemStack<T extends TileEntity & ITileInteractable>
    extends IInteraction<T> {

  interface IInsertionIndexProvider<T extends TileEntity & ITileInteractable> {

    IInsertionIndexProvider ZERO = (tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ) -> 0;

    static <T extends TileEntity & ITileInteractable> IInsertionIndexProvider<T> zero() {

      //noinspection unchecked
      return ZERO;
    }

    int getInsertionIndex(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ);
  }

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
  boolean isItemStackValid(ItemStack itemStack);

  /**
   * @return true if this interaction handler is empty
   */
  boolean isEmpty();

  ItemStack getStackInSlot();

  ItemStack extract(int amount, boolean simulate);

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
