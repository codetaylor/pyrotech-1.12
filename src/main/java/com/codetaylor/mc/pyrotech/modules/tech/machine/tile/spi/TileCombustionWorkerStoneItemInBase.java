package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCombustionWorkerStoneItemInBase<E extends StoneMachineRecipeItemInBase<E>>
    extends TileCombustionWorkerStoneBase<E> {

  private InputStackHandler inputStackHandler;

  public TileCombustionWorkerStoneItemInBase() {

    this.inputStackHandler = new InputStackHandler(this, 1);
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.recalculateRemainingTime(handler.getStackInSlot(slot));
      this.markDirty();
    });

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler)
    });
  }

  @Override
  public boolean hasInput() {

    return !this.inputStackHandler.getStackInSlot(0).isEmpty();
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (facing == EnumFacing.UP && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      if (facing == EnumFacing.UP) {
        //noinspection unchecked
        return (T) this.inputStackHandler;
      }
    }

    return super.getCapability(capability, facing);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final TileCombustionWorkerStoneItemInBase<E> tile;

    /* package */ InputStackHandler(TileCombustionWorkerStoneItemInBase<E> tile, int size) {

      super(size);
      this.tile = tile;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return MathHelper.clamp(getInputSlotSize(), 1, 64);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      // Filter out non-recipe items.

      ItemStack remainingItems = super.insertItem(slot, stack, true);

      if (remainingItems.getCount() == stack.getCount()) {
        return stack;
      }

      E recipe = this.tile.getRecipe(stack);

      if (recipe == null) {
        return stack;
      }

      remainingItems.setCount(stack.getCount() - remainingItems.getCount());

      if (!this.tile.allowInsertInput(remainingItems, recipe)) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

  protected abstract int getInputSlotSize();

}
