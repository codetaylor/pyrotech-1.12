package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

import com.codetaylor.mc.pyrotech.library.fluid.CPacketFluidUpdate;
import com.codetaylor.mc.pyrotech.library.fluid.FluidTankBase;
import com.codetaylor.mc.pyrotech.library.fluid.IFluidTankUpdateListener;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class TileTarTankBase
    extends TileEntity
    implements ITickable,
    CPacketFluidUpdate.IFluidUpdatePacketConsumer,
    IFluidTankUpdateListener {

  private static final int UPDATE_DELAY_TICKS = 5;

  protected FluidTankBase fluidTank;
  protected int ticksUntilNextUpdate;

  public TileTarTankBase() {

    this.fluidTank = new FluidTankBase(this.getTankCapacity(), this);
  }

  public FluidTank getFluidTank() {

    return this.fluidTank;
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.ticksUntilNextUpdate > 0) {
      this.ticksUntilNextUpdate -= 1;

    } else {
      this.ticksUntilNextUpdate = UPDATE_DELAY_TICKS;
      this.collect(this.pos, this.fluidTank, this.world);
    }
  }

  private void collect(BlockPos pos, FluidTank fluidTank, World world) {

    if (fluidTank.getFluidAmount() == fluidTank.getCapacity()) {
      return;
    }

    List<BlockPos> sourcePositions = this.getCollectionSourcePositions(world, pos);

    for (BlockPos sourcePosition : sourcePositions) {
      TileEntity tileEntity = world.getTileEntity(sourcePosition);
      FluidTank sourceFluidTank = this.getCollectionSourceFluidTank(tileEntity);

      if (sourceFluidTank != null) {
        this.collect(sourceFluidTank, fluidTank);
      }
    }
  }

  private void collect(FluidTank source, FluidTank target) {

    if (source.getFluidAmount() > 0) {
      source.drain(target.fillInternal(source.getFluid(), true), true);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onFluidUpdatePacket(CPacketFluidUpdate packet) {

    FluidStack fluidStack = packet.getFluidStack();
    this.fluidTank.setFluid(fluidStack);
  }

  @Override
  public void onTankContentsChanged(FluidTank fluidTank) {

    if (!this.world.isRemote) {
      ModulePyrotech.PACKET_SERVICE.sendToAllAround(new CPacketFluidUpdate(this.pos, fluidTank.getFluid()), this);
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("fluidTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.fluidTank.readFromNBT(compound.getCompoundTag("fluidTank"));
  }

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
  }

  protected abstract List<BlockPos> getCollectionSourcePositions(World world, BlockPos origin);

  protected abstract int getTankCapacity();

  @Nullable
  protected abstract FluidTank getCollectionSourceFluidTank(@Nullable TileEntity tileEntity);
}
