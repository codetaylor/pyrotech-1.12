package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileDryingRack
    extends TileEntity
    implements ITickable {

  private ItemStackHandler stackHandler;
  private ItemStackHandler outputStackHandler;
  private int[] dryTimeTotal;
  private int[] dryTimeRemaining;

  public TileDryingRack() {

    this.stackHandler = new ItemStackHandler(4) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileDryingRack.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileDryingRack.this.world, TileDryingRack.this.pos);
      }
    };

    this.outputStackHandler = new ItemStackHandler(4) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileDryingRack.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileDryingRack.this.world, TileDryingRack.this.pos);
      }
    };

    this.dryTimeTotal = new int[4];
    this.dryTimeRemaining = new int[4];
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public void removeItems() {

    if (this.world.isRemote) {
      return;
    }

    for (int i = 0; i < this.stackHandler.getSlots(); i++) {
      ItemStack itemStack = this.stackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
      }
    }

    for (int i = 0; i < this.outputStackHandler.getSlots(); i++) {
      ItemStack itemStack = this.outputStackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
      }
    }
  }

  @Override
  public void update() {

  }

  @Override
  public boolean shouldRefresh(
      World world,
      BlockPos pos,
      @Nonnull IBlockState oldState,
      @Nonnull IBlockState newState
  ) {

    if (oldState.getBlock() == newState.getBlock()) {
      return false;
    }

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.dryTimeTotal = compound.getIntArray("dryTimeTotal");
    this.dryTimeRemaining = compound.getIntArray("dryTimeRemaining");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setIntArray("dryTimeTotal", this.dryTimeTotal);
    compound.setIntArray("dryTimeRemaining", this.dryTimeRemaining);
    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Synchronization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

}
