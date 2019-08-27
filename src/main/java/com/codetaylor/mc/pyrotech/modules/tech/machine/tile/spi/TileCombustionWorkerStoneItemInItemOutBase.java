package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.LargeDynamicStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileCombustionWorkerStoneItemInItemOutBase<E extends MachineRecipeItemInItemOutBase<E>>
    extends TileCombustionWorkerStoneItemInBase<E> {

  private OutputStackHandler outputStackHandler;

  public TileCombustionWorkerStoneItemInItemOutBase() {

    this.outputStackHandler = new OutputStackHandler(9);
    this.outputStackHandler.addObserver((handler, slot) -> {
      this.resetDormantCounter();
      this.markDirty();
    });

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.outputStackHandler)
    });

    this.addInteractions(new IInteraction[]{
        new Interaction(this, new ItemStackHandler[]{
            this.getInputStackHandler(),
            this.outputStackHandler
        })
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public OutputStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  @Override
  public boolean allowInsertInput(ItemStack stack, E recipe) {

    return this.outputStackHandler.getStackInSlot(0).isEmpty();
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()) {
      return false;
    }

    return (facing == EnumFacing.DOWN && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (!this.allowAutomation()) {
      return null;
    }

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

      if (facing == EnumFacing.DOWN) {
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

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);
    E recipe = this.getRecipe(input);

    if (recipe != null) {
      this.getInputStackHandler().setStackInSlot(0, ItemStack.EMPTY);

      List<ItemStack> outputItems = this.getRecipeOutput(recipe, input, new ArrayList<>());

      for (ItemStack outputItem : outputItems) {
        this.outputStackHandler.insertItem(outputItem, false);
      }
    }
  }

  protected abstract List<ItemStack> getRecipeOutput(E recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks);

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
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
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
    protected boolean doExtract(EnumType type, World world, EntityPlayer player, BlockPos tilePos) {

      // Extract all slots in the output stack handler.

      ItemStackHandler outputStackHandler = this.stackHandlers[1];

      int slots = outputStackHandler.getSlots();

      for (int i = 1; i < slots; i++) {
        ItemStack extractItem = outputStackHandler.extractItem(i, outputStackHandler.getStackInSlot(i).getCount(), world.isRemote);

        if (!extractItem.isEmpty()
            && !world.isRemote) {
          StackHelper.addToInventoryOrSpawn(world, player, extractItem, tilePos, 1.0, false, (type == EnumType.MouseClick));
        }
      }

      return super.doExtract(type, world, player, tilePos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public class OutputStackHandler
      extends LargeDynamicStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler(int size) {

      super(size);
    }
  }

}
