package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.IObservableStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.FluidHelper;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCombustionWorkerStoneItemInFluidOutBase<E extends MachineRecipeItemInFluidOutBase<E>>
    extends TileCombustionWorkerStoneItemInBase<E> {

  private OutputFluidTank outputFluidTank;

  public TileCombustionWorkerStoneItemInFluidOutBase() {

    this.outputFluidTank = new OutputFluidTank(this, this.getOutputFluidTankSize());
    this.outputFluidTank.addObserver((handler, slot) -> {
      this.resetDormantCounter();
      this.markDirty();
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
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

  protected abstract int getHotFluidTemperature();

  protected abstract boolean canHoldHotFluids();

  public FluidTank getOutputFluidTank() {

    return this.outputFluidTank;
  }

  protected int getOutputFluidTankSize() {

    return 4000;
  }

  @Override
  public boolean allowInsertInput(ItemStack stack, E recipe) {

    FluidStack fluid = this.outputFluidTank.getFluid();

    // Prevent an item from being inserted if its output fluid
    // does not match the existing fluid.
    if (fluid != null
        && recipe.getOutput().getFluid() != fluid.getFluid()) {
      return false;
    }

    return fluid == null
        || fluid.amount == 0;
  }

  @Override
  protected int getAllowedRecipeInputQuantity(ItemStack insertedItems, E recipe) {

    ItemStack stackInSlot = this.getInputStackHandler().getStackInSlot(0);
    FluidStack output = recipe.getOutput();
    int outputAmount = output.amount;
    int result = 0;

    for (int i = 0; i < insertedItems.getCount(); i++) {
      output.amount = outputAmount * stackInSlot.getCount() + outputAmount * (i + 1);

      if (this.getOutputFluidTank().fill(output, false) == output.amount) {
        result = i + 1;

      } else {
        break;
      }
    }

    return result;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()) {
      return false;
    }

    return (facing != EnumFacing.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()) {
      return null;
    }

    if (facing != EnumFacing.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {

      //noinspection unchecked
      return (T) this.outputFluidTank;
    }

    return super.getCapability(capability, facing);
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  protected void onRecipeComplete() {

    ItemStackHandler inputStackHandler = this.getInputStackHandler();
    ItemStack input = inputStackHandler.getStackInSlot(0);
    E recipe = this.getRecipe(input);

    if (recipe != null) {

      if (this.processAsynchronous()) {
        inputStackHandler.setStackInSlot(0, ItemStack.EMPTY);
        FluidStack recipeOutput = this.getRecipeOutput(recipe, input);
        this.outputFluidTank.fill(recipeOutput, true);

      } else {
        StackHelper.decreaseStackInSlot(inputStackHandler, 0, 1, false);
        FluidStack recipeOutput = this.getRecipeOutput(recipe, input);
        this.outputFluidTank.fill(recipeOutput, true);
      }

      FluidStack fluid = this.outputFluidTank.getFluid();

      if (fluid != null) {
        FluidHelper.playFluidFillSoundServer(
            fluid.getFluid(),
            this.world,
            this.pos
        );
      }
    }
  }

  protected abstract FluidStack getRecipeOutput(E recipe, ItemStack input);

  public boolean processAsynchronous() {

    return true;
  }

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

  @Override
  protected IObservableStackHandler.IContentsChangedEventHandler getInputStackHandlerObserver() {

    if (this.processAsynchronous()) {
      return super.getInputStackHandlerObserver();
    }

    return (handler, slot) -> {
      this.recalculateRemainingTime(handler.getStackInSlot(slot));
      this.markDirty();
    };
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
      extends InteractionItemStack<TileCombustionWorkerStoneItemInFluidOutBase> {

    private final TileCombustionWorkerStoneItemInFluidOutBase<E> tile;

    /* package */ Interaction(TileCombustionWorkerStoneItemInFluidOutBase<E> tile, ItemStackHandler[] stackHandlers) {

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

      E recipe = this.tile.getRecipe(itemStack);

      if (recipe == null) {
        return false;
      }

      // Check quantity
      int quantity = this.tile.getAllowedRecipeInputQuantity(itemStack, recipe);

      return (quantity > 0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class OutputFluidTank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileCombustionWorkerStoneItemInFluidOutBase<E> tile;

    /* package */ OutputFluidTank(TileCombustionWorkerStoneItemInFluidOutBase<E> tile, int size) {

      super(size);
      this.tile = tile;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {

      int filled = super.fillInternal(resource, doFill);

      if (!this.tile.canHoldHotFluids()) {
        World world = this.tile.world;
        BlockPos pos = this.tile.pos;

        if (resource != null) {
          Fluid fluid = resource.getFluid();

          if (fluid.getTemperature(resource) >= this.tile.getHotFluidTemperature()) {

            if (!world.isRemote) {
              world.setBlockToAir(pos);
              world.setBlockToAir(pos.up());
              SoundHelper.playSoundServer(world, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
              FluidUtil.tryPlaceFluid(null, world, pos, this, resource);
            }
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
          }
        }
      }

      return filled;
    }
  }

}
