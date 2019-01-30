package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class TileTarTankBase
    extends TileNetBase
    implements ITickable {

  private static final int COLLECT_INTERVAL_TICKS = 20;

  protected Tank fluidTank;
  private TickCounter collectionTickCounter;

  public TileTarTankBase() {

    super(ModuleTechRefractory.TILE_DATA_SERVICE);

    this.fluidTank = new Tank(this.getTankCapacity());
    this.fluidTank.addObserver((tank, amount) -> {
      this.markDirty();
    });

    this.collectionTickCounter = new TickCounter(COLLECT_INTERVAL_TICKS);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataFluidTank<>(this.fluidTank)
    });
  }

  public FluidTank getFluidTank() {

    return this.fluidTank;
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.collectionTickCounter.increment()) {
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

  public static class Tank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    /* package */ Tank(int capacity) {

      super(capacity);
    }
  }
}
