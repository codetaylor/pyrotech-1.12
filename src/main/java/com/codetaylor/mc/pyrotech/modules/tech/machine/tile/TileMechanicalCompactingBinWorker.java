package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.inventory.LargeObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataLargeItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.FacingHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionItemStack;
import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileSoakingPot;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMechanicalCompactingBinWorker
    extends TileCogWorkerBase {

  private OutputStackHandler outputStackHandler;

  public TileMechanicalCompactingBinWorker() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.outputStackHandler = new OutputStackHandler(this.getOutputCapacity());
    this.outputStackHandler.addObserver((handler, slot) -> this.markDirty());

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataLargeItemStackHandler<>(this.outputStackHandler)
    });

    this.addInteractions(new IInteraction[]{
        new InteractionItem(this.outputStackHandler)
    });
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (facing == EnumFacing.DOWN)
        && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (facing == EnumFacing.DOWN
        && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.outputStackHandler;
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public OutputStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  @Override
  protected int getUpdateIntervalTicks() {

    return ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.WORK_INTERVAL_TICKS;
  }

  @Override
  protected boolean isValidCog(ItemStack itemStack) {

    return (this.getCogRecipeProgress(itemStack) > -1);
  }

  protected double getCogRecipeProgress(ItemStack itemStack) {

    ResourceLocation registryName = itemStack.getItem().getRegistryName();
    return ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.getCogRecipeProgress(registryName);
  }

  protected int getOutputCapacity() {

    return ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.OUTPUT_CAPACITY;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  protected int doWork(ItemStack cog) {

    IBlockState blockState = this.world.getBlockState(this.pos);
    EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
    TileEntity tileEntity = this.world.getTileEntity(this.pos.offset(facing));

    if (tileEntity instanceof TileCompactingBin) {
      TileCompactingBin tile = (TileCompactingBin) tileEntity;
      CompactingBinRecipeBase currentRecipe = tile.getCurrentRecipe();

      if (currentRecipe != null
          && currentRecipe.getAmount() <= tile.getInputStackHandler().getTotalItemCount()
          && this.outputStackHandler.insertItemInternal(currentRecipe.getOutput(), true).getCount() == 0) {

        float progress = (float) MathHelper.clamp(this.getCogRecipeProgress(this.getCog()), 0, 1);

        if (tile.addRecipeProgress(progress) > 0.9999) {
          // recipe complete
          this.outputStackHandler.insertItemInternal(currentRecipe.getOutput(), false);

          // reduce input items
          tile.getInputStackHandler().removeItems(currentRecipe.getAmount());
        }

        SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f);

        return 1;
      }
    }

    return -1;
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
  // - Interaction
  // ---------------------------------------------------------------------------

  protected Transform getCogInteractionTransform() {

    return new Transform(
        Transform.translate(14.0 / 16.0, 8.0 / 16.0, 0.5),
        Transform.rotate(0, 1, 0, 90),
        Transform.scale(0.75, 0.75, 2.00)
    );
  }

  protected AxisAlignedBB getCogInteractionBounds() {

    return AABBHelper.create(12, 0, 0, 16, 16, 16);
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    if (blockState.getBlock() == ModuleTechMachine.Blocks.MECHANICAL_COMPACTING_BIN) {
      return FacingHelper.rotateFacingCW(blockState.getValue(Properties.FACING_HORIZONTAL));
    }

    return super.getTileFacing(world, pos, blockState);
  }

  private class InteractionItem
      extends InteractionItemStack<TileSoakingPot> {

    /* package */ InteractionItem(ItemStackHandler stackHandler) {

      super(new ItemStackHandler[]{stackHandler}, 0, new EnumFacing[]{EnumFacing.UP},
          AABBHelper.create(1, 16, 3, 11, 11, 13),
          new Transform(
              Transform.translate(6.0 / 16.0, 1.0, 0.5),
              Transform.rotate(),
              Transform.scale(8.0 / 16.0, 8.0 / 16.0, 8.0 / 16.0)
          )
      );
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return false;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public static class OutputStackHandler
      extends LargeObservableStackHandler
      implements ITileDataItemStackHandler {

    private final int capacity;

    /* package */ OutputStackHandler(int capacity) {

      super(1);
      this.capacity = capacity;
    }

    @Override
    public int getSlotLimit(int slot) {

      return this.capacity;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      return stack;
    }

    @Nonnull
      /* package */ ItemStack insertItemInternal(@Nonnull ItemStack stack, boolean simulate) {

      return super.insertItem(0, stack, simulate);
    }
  }
}
