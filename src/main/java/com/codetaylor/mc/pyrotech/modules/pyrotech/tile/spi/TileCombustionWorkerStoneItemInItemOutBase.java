package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.StoneMachineRecipeBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileCombustionWorkerStoneItemInItemOutBase<E extends StoneMachineRecipeBase<E>>
    extends TileCombustionWorkerStoneBase<E> {

  private InputStackHandler inputStackHandler;
  private OutputStackHandler outputStackHandler;

  public TileCombustionWorkerStoneItemInItemOutBase() {

    this.inputStackHandler = new InputStackHandler(this, 1);
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.recalculateRemainingTime(handler.getStackInSlot(slot));
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler(9);
    this.outputStackHandler.addObserver((handler, slot) -> {
      this.resetDormantCounter();
      this.markDirty();
    });

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler),
        new TileDataItemStackHandler<>(this.outputStackHandler)
    });

    this.addInteractions(new IInteraction[]{
        new Interaction(this, new ItemStackHandler[]{
            this.inputStackHandler,
            this.outputStackHandler
        })
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (facing != null && (facing == EnumFacing.UP || facing == EnumFacing.DOWN) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      if (facing == EnumFacing.UP) {
        //noinspection unchecked
        return (T) this.inputStackHandler;

      } else if (facing == EnumFacing.DOWN) {
        //noinspection unchecked
        return (T) this.outputStackHandler;
      }
    }

    return super.getCapability(capability, facing);
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  protected void onRecipeComplete() {

    // set stack handler items to recipe result

    ItemStack input = this.inputStackHandler.getStackInSlot(0);
    E recipe = this.getRecipe(input);

    if (recipe != null) {
      this.inputStackHandler.setStackInSlot(0, ItemStack.EMPTY);

      List<ItemStack> outputItems = this.getRecipeOutput(recipe, input, new ArrayList<>());

      for (ItemStack outputItem : outputItems) {
        this.insertOutputItem(outputItem);
      }
    }
  }

  protected void insertOutputItem(ItemStack output) {

    for (int i = 0; i < 9 && !output.isEmpty(); i++) {
      output = this.outputStackHandler.insertItem(i, output, false);
    }
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

  @Override
  public boolean hasInput() {

    return !this.inputStackHandler.getStackInSlot(0).isEmpty();
  }

  public void dropContents() {

    ItemStackHandler stackHandler = this.getInputStackHandler();
    ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getStackInSlot(0).getCount(), false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    stackHandler = this.getOutputStackHandler();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      itemStack = stackHandler.extractItem(i, stackHandler.getStackInSlot(i).getCount(), false);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
      }
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
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
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

    @Override
    protected boolean doExtract(World world, EntityPlayer player, BlockPos tilePos) {

      // Extract all slots in the output stack handler.

      ItemStackHandler outputStackHandler = this.stackHandlers[1];

      int slots = outputStackHandler.getSlots();

      for (int i = 1; i < slots; i++) {
        ItemStack extractItem = outputStackHandler.extractItem(i, outputStackHandler.getStackInSlot(i).getCount(), world.isRemote);

        if (!extractItem.isEmpty()
            && !world.isRemote) {
          StackHelper.addToInventoryOrSpawn(world, player, extractItem, tilePos);
        }
      }

      return super.doExtract(world, player, tilePos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final TileCombustionWorkerStoneItemInItemOutBase<E> tile;

    /* package */ InputStackHandler(TileCombustionWorkerStoneItemInItemOutBase<E> tile, int size) {

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

      StoneMachineRecipeBase<E> recipe = this.tile.getRecipe(stack);

      if (recipe == null
          || !this.tile.getOutputStackHandler().getStackInSlot(0).isEmpty()) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }
  }

  protected abstract int getInputSlotSize();

  private class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler(int size) {

      super(size);
    }
  }

}
