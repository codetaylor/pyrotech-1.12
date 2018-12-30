package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.StoneMachineRecipeItemInFluidOutBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCombustionWorkerStoneItemInFluidOutBase<E extends StoneMachineRecipeItemInFluidOutBase<E>>
    extends TileCombustionWorkerStoneItemInBase<E> {

  private OutputFluidTank outputFluidTank;

  public TileCombustionWorkerStoneItemInFluidOutBase() {

    this.outputFluidTank = new OutputFluidTank(this.getOutputFluidTankSize());
    this.outputFluidTank.addObserver((handler, slot) -> {
      this.resetDormantCounter();
      this.markDirty();
    });

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataFluidTank<>(this.outputFluidTank)
    });

    this.addInteractions(new IInteraction[]{
        new Interaction(this, new ItemStackHandler[]{
            this.getInputStackHandler()
        })
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public FluidTank getOutputFluidTank() {

    return this.outputFluidTank;
  }

  protected int getOutputFluidTankSize() {

    return 4000;
  }

  @Override
  public boolean allowInsertInput(ItemStack stack, E recipe) {

    FluidStack fluid = this.outputFluidTank.getFluid();

    return fluid == null
        || fluid.amount == 0;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (facing == EnumFacing.DOWN && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {

      if (facing == EnumFacing.DOWN) {
        //noinspection unchecked
        return (T) this.outputFluidTank;
      }
    }

    return super.getCapability(capability, facing);
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  protected void onRecipeComplete() {

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);
    E recipe = this.getRecipe(input);

    if (recipe != null) {
      this.getInputStackHandler().setStackInSlot(0, ItemStack.EMPTY);
      FluidStack recipeOutput = this.getRecipeOutput(recipe, input);
      this.outputFluidTank.fill(recipeOutput, true);
    }
  }

  protected abstract FluidStack getRecipeOutput(E recipe, ItemStack input);

  // ---------------------------------------------------------------------------
  // - Worker
  // ---------------------------------------------------------------------------

  @Override
  protected float workerCalculateProgress(int taskIndex) {

    ItemStack itemStack = this.getInputStackHandler().getStackInSlot(0);

    if (itemStack.isEmpty()) {
      return 0;
    }

    E recipe = this.getRecipe(itemStack);

    if (recipe == null) {
      // Should never happen because we filter items on input.
      return 0;
    }

    return 1f - (this.getRemainingRecipeTimeTicks() / (float) recipe.getTimeTicks());
  }

  public void dropContents() {

    ItemStackHandler stackHandler = this.getInputStackHandler();
    ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getStackInSlot(0).getCount(), false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    super.dropContents();
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("outputFluidTank", this.outputFluidTank.writeToNBT(new NBTTagCompound()));
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.outputFluidTank.readFromNBT(compound.getCompoundTag("outputFluidTank"));
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  private class Interaction
      extends InteractionItemStack<TileCombustionWorkerStoneBase> {

    private final TileCombustionWorkerStoneBase<E> tile;

    /* package */ Interaction(TileCombustionWorkerStoneBase<E> tile, ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          tile.getInputInteractionSides(),
          tile.getInputInteractionBoundsTop(),
          new Transform(
              Transform.translate(0.5, 1.2, 0.5),
              Transform.rotate(),
              Transform.scale(0.5, 0.5, 0.5)
          )
      );
      this.tile = tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (this.tile.getRecipe(itemStack) != null);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class OutputFluidTank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    /* package */ OutputFluidTank(int size) {

      super(size);
    }
  }

}
