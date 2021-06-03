package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.IObservableStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.ParticleHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCombustionWorkerStoneItemInBase<E extends MachineRecipeItemInBase<E>>
    extends TileCombustionWorkerStoneBase<E> {

  private final InputStackHandler inputStackHandler;

  public TileCombustionWorkerStoneItemInBase() {

    this.inputStackHandler = new InputStackHandler(this, 1);
    this.inputStackHandler.addObserver(this.getInputStackHandlerObserver());

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler)
    });
  }

  protected IObservableStackHandler.IContentsChangedEventHandler getInputStackHandlerObserver() {

    return new Observer<>(this);
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

    if (!this.allowAutomation()) {
      return false;
    }

    return (facing == EnumFacing.UP && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()) {
      return null;
    }

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

  @Override
  public void update() {

    super.update();

    if (this.world.isRemote
        && ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES
        && this.workerIsActive()
        && !this.getInputStackHandler().getStackInSlot(0).isEmpty()
        && this.world.getTotalWorldTime() % 40 == 0) {
      ParticleHelper.spawnProgressParticlesClient(
          1,
          this.pos.getX() + 0.5, this.pos.getY() + 1.625, this.pos.getZ() + 0.5,
          0.25, 0.25, 0.25
      );
    }
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
        // If no items can be placed in, abort
        return stack;
      }

      E recipe = this.tile.getRecipe(stack);

      if (recipe == null) {
        // If no recipe exists for the input items, abort
        return stack;
      }

      ItemStack insertedItems = stack.copy();
      insertedItems.setCount(stack.getCount() - remainingItems.getCount());

      if (!this.tile.allowInsertInput(insertedItems, recipe)) {
        return stack;
      }

      // Check quantity
      int quantity = this.tile.getAllowedRecipeInputQuantity(insertedItems, recipe);

      if (quantity == 0) {
        return stack;

      } else {

        if (quantity < insertedItems.getCount()) {
          insertedItems.setCount(quantity);
        }

        super.insertItem(slot, insertedItems, simulate);

        ItemStack toReturn = stack.copy();
        toReturn.setCount(stack.getCount() - insertedItems.getCount());
        return toReturn;
      }
    }
  }

  protected int getAllowedRecipeInputQuantity(ItemStack insertedItems, E recipe) {

    return insertedItems.getCount();
  }

  public static class Observer<E extends MachineRecipeBase<E>>
      implements IObservableStackHandler.IContentsChangedEventHandler {

    private final TileCombustionWorkerStoneBase<E> tile;

    private int lastItemCount;

    private Observer(TileCombustionWorkerStoneBase<E> tile) {

      this.tile = tile;
      this.lastItemCount = -1;
    }

    @Override
    public void onContentsChanged(ItemStackHandler handler, int slot) {

      if (this.lastItemCount <= 0
          && handler.getStackInSlot(slot).getCount() > 0) {
        this.tile.recalculateRemainingTime(handler.getStackInSlot(slot));
      }
      this.tile.markDirty();
      this.lastItemCount = handler.getStackInSlot(slot).getCount();
    }
  }

  protected abstract int getInputSlotSize();

}
