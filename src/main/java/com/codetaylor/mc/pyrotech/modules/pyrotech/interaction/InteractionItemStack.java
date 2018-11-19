package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class InteractionHandlerItemStack
    implements IInteractionHandlerItemStack {

  protected final ItemStackHandler[] stackHandlers;
  protected final int slot;
  protected final int sides;
  protected final InteractionBounds bounds;
  protected final Transform transform;

  public InteractionHandlerItemStack(
      ItemStackHandler[] stackHandlers,
      int slot,
      EnumFacing[] sides,
      InteractionBounds bounds,
      Transform transform
  ) {

    this.stackHandlers = stackHandlers;
    this.slot = slot;
    this.bounds = bounds;
    this.transform = transform;

    int sidesEncoded = 0;

    for (EnumFacing side : sides) {
      sidesEncoded |= side.getIndex();
    }

    this.sides = sidesEncoded;
  }

  @Override
  public Transform getTransform(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack) {

    return this.transform;
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
   * This method will be called every frame when a player is looking at an
   * interaction handler and, as such, should be fast. Take care to cache and
   * optimize accordingly.
   *
   * @param itemStack the {@link ItemStack} to insert
   * @return true if the given {@link ItemStack} is allowed to be inserted
   */
  @Override
  public boolean isItemStackValid(ItemStack itemStack) {

    return true;
  }

  /**
   * Searches each stack handler in the order they are provided in the
   * constructor. Returns the first non-empty {@link ItemStack} found.
   *
   * @return the first {@link ItemStack} found
   */
  @Override
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
  @Override
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
  @Override
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

  @Override
  public boolean isEmpty() {

    return this.getStackInSlot().isEmpty();
  }

  @Override
  public boolean canInteractWith(World world, EnumFacing hitSide, BlockPos hitPos, Vec3d hitVec, BlockPos pos, IBlockState blockState) {

    double hitX = hitVec.x - pos.getX();
    double hitY = hitVec.y - pos.getY();
    double hitZ = hitVec.z - pos.getZ();

    double x = 0;
    double y = 0;

    // TODO: test this is right
    switch (hitSide) {
      case UP:
        x = hitX;
        y = hitZ;
        break;
      case DOWN:
        x = 1.0 - hitX;
        y = hitZ;
        break;
      case NORTH: // toward -Z
        x = 1.0 - hitX;
        y = hitY;
        break;
      case SOUTH: // toward +Z
        x = hitX;
        y = hitY;
        break;
      case EAST: // toward +X
        x = hitZ;
        y = hitY;
        break;
      case WEST: // toward -X
        x = 1.0 - hitZ;
        y = hitY;
        break;
    }

    return this.canInteractWithActualSide(world, this.getActualSideHit(hitSide), x, y, pos, blockState);
  }

  protected EnumFacing getActualSideHit(EnumFacing sideHit) {

    return sideHit;
  }

  protected boolean canInteractWithActualSide(World world, EnumFacing actualSide, double hitX, double hitY, BlockPos pos, IBlockState blockState) {

    return ((this.sides & actualSide.getIndex()) == actualSide.getIndex())
        && this.bounds.contains(hitX, hitY);
  }

}
