package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import static net.minecraft.tileentity.TileEntity.INFINITE_EXTENT_AABB;

public abstract class InteractionHandler_ItemStack_Base {

  protected final ItemStackHandler[] stackHandlers;
  protected final int slot;
  protected final int sides;
  protected final AxisAlignedBB bounds;

  public InteractionHandler_ItemStack_Base(ItemStackHandler[] stackHandlers, int slot) {

    this(stackHandlers, slot, EnumFacing.VALUES, INFINITE_EXTENT_AABB);
  }

  public InteractionHandler_ItemStack_Base(ItemStackHandler[] stackHandlers, int slot, EnumFacing[] sides, AxisAlignedBB bounds) {

    this.stackHandlers = stackHandlers;
    this.slot = slot;
    this.bounds = bounds;

    int sidesEncoded = 0;

    for (EnumFacing side : sides) {
      sidesEncoded |= side.getIndex();
    }

    this.sides = sidesEncoded;
  }

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
  public abstract Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack);

  /**
   * Returns true if the given {@link ItemStack} is allowed to be inserted.
   * <p>
   * <p>
   * This is used as a check before attempting to insert an item, therefore,
   * this method should only be concerned with the type of item and not
   * whether the item's quantity will fit into the slot.
   * <p>
   * <p>
   * This method will be called every frame when a player is looking at an
   * interaction handler and, as such, should be fast. Take care to cache and
   * optimize accordingly.
   *
   * @param itemStack the {@link ItemStack} to insert
   * @return true if the given {@link ItemStack} is allowed to be inserted
   */
  public boolean isItemStackValid(ItemStack itemStack) {

    return true;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Returns the first non-empty {@link ItemStack} found.
   *
   * @return the first {@link ItemStack} found
   */
  public ItemStack getStackInSlot() {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      ItemStack itemStack = this.stackHandlers[i].getStackInSlot(this.slot);

      if (!itemStack.isEmpty()) {
        return itemStack;
      }
    }

    return ItemStack.EMPTY;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Returns the first non-empty {@link ItemStack} extracted.
   *
   * @param amount   the amount to extract
   * @param simulate set false to actually perform the extraction
   * @return the first non-empty {@link ItemStack} extracted
   */
  public ItemStack extract(int amount, boolean simulate) {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      ItemStack itemStack = this.stackHandlers[i].getStackInSlot(this.slot);

      if (!itemStack.isEmpty()) {
        return this.stackHandlers[i].extractItem(this.slot, amount, simulate);
      }
    }

    return ItemStack.EMPTY;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Inserts into the first handler that will accept any of the
   * given {@link ItemStack}.
   * <p>
   * Returns any remaining items in an {@link ItemStack}.
   *
   * @param itemStack the {@link ItemStack} to insert
   * @param simulate  set false to actually perform the insertion
   * @return any remaining items in an {@link ItemStack}
   */
  public ItemStack insert(ItemStack itemStack, boolean simulate) {

    if (!this.isItemStackValid(itemStack)) {
      return itemStack;
    }

    for (int i = 0; i < this.stackHandlers.length; i++) {
      int count = itemStack.getCount();
      ItemStack result = this.stackHandlers[i].insertItem(this.slot, itemStack, true);

      if (result.getCount() != count) {
        return this.stackHandlers[i].insertItem(this.slot, itemStack, simulate);
      }
    }

    return itemStack;
  }

  /**
   * @return true if the entire handler stack is empty
   */
  public boolean isEmpty() {

    return this.getStackInSlot().isEmpty();
  }

  /**
   * Returns true if the given {@link RayTraceResult} intersects this handler.
   *
   * @param rayTraceResult the {@link RayTraceResult} to check
   * @return true if the given {@link RayTraceResult} intersects this handler
   */
  public boolean intersects(RayTraceResult rayTraceResult) {

    if (rayTraceResult == null) {
      return false;
    }

    if (!this.canInteractWithSide(rayTraceResult.sideHit)) {
      return false;
    }

    BlockPos blockPos = rayTraceResult.getBlockPos();
    double hitX = rayTraceResult.hitVec.x - blockPos.getX();
    double hitY = rayTraceResult.hitVec.y - blockPos.getY();
    double hitZ = rayTraceResult.hitVec.z - blockPos.getZ();

    return this.canInteractAtPosition(hitX, hitY, hitZ);
  }

  /**
   * Returns true if this handler allows interaction on the given side.
   *
   * @param side the side to test for interaction
   * @return true if this handler allows interaction on the given side
   */
  public boolean canInteractWithSide(EnumFacing side) {

    return ((this.sides & side.getIndex()) == side.getIndex());
  }

  /**
   * Returns true if this handler allows interaction at the given position.
   * <p>
   * Position is relative to the block position with values in the range [0,1].
   *
   * @param hitX
   * @param hitY
   * @param hitZ
   * @return true if this handler allows interaction at the given position
   */
  @SuppressWarnings("JavaDoc")
  public boolean canInteractAtPosition(double hitX, double hitY, double hitZ) {

    return AABBHelper.contains(this.bounds, hitX, hitY, hitZ);
  }
}
