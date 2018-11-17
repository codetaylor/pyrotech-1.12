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

public abstract class InteractionHandlerItemStackBase {

  protected final ItemStackHandler[] stackHandlers;
  protected final int slot;
  protected final int sides;
  protected final AxisAlignedBB bounds;

  public InteractionHandlerItemStackBase(ItemStackHandler[] stackHandlers, int slot, EnumFacing[] sides, AxisAlignedBB bounds) {

    this.stackHandlers = stackHandlers;
    this.slot = slot;
    this.bounds = bounds;

    int sidesEncoded = 0;

    for (EnumFacing side : sides) {
      sidesEncoded |= side.getIndex();
    }

    this.sides = sidesEncoded;
  }

  public abstract InteractionHandler.Transforms getTransforms(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack);

  public ItemStack getStackInSlot() {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      ItemStack itemStack = this.stackHandlers[i].getStackInSlot(this.slot);

      if (!itemStack.isEmpty()) {
        return itemStack;
      }
    }

    return ItemStack.EMPTY;
  }

  public ItemStack extract(int amount, boolean simulate) {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      ItemStack itemStack = this.stackHandlers[i].getStackInSlot(this.slot);

      if (!itemStack.isEmpty()) {
        return this.stackHandlers[i].extractItem(this.slot, amount, simulate);
      }
    }

    return ItemStack.EMPTY;
  }

  public ItemStack insert(ItemStack itemStack, boolean simulate) {

    for (int i = 0; i < this.stackHandlers.length; i++) {
      int count = itemStack.getCount();
      ItemStack result = this.stackHandlers[i].insertItem(this.slot, itemStack, true);

      if (result.getCount() != count) {
        return this.stackHandlers[i].insertItem(this.slot, itemStack, simulate);
      }
    }

    return itemStack;
  }

  public boolean isEmpty() {

    return this.getStackInSlot().isEmpty();
  }

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

  protected boolean canInteractWithSide(EnumFacing side) {

    return ((this.sides & side.getIndex()) == side.getIndex());
  }

  protected boolean canInteractAtPosition(double hitX, double hitY, double hitZ) {

    return AABBHelper.contains(this.bounds, hitX, hitY, hitZ);
  }
}
