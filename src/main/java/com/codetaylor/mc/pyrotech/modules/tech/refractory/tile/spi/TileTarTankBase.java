package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class TileTarTankBase
    extends TileNetBase
    implements ITickable {

  private static final int COLLECT_INTERVAL_TICKS = 20;

  protected Tank fluidTank;
  private TickCounter collectionTickCounter;

  public TileTarTankBase() {

    super(ModuleTechRefractory.TILE_DATA_SERVICE);

    this.fluidTank = new Tank(this, this.getTankCapacity());
    this.fluidTank.addObserver((tank, amount) -> {
      this.markDirty();
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    });

    this.collectionTickCounter = new TickCounter(COLLECT_INTERVAL_TICKS);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataFluidTank<>(this.fluidTank)
    });
  }

  protected abstract int getHotFluidTemperature();

  protected abstract boolean canHoldHotFluids();

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
    List<FluidTank> fluidTanks = new ArrayList<>(sourcePositions.size());

    for (BlockPos sourcePosition : sourcePositions) {
      TileEntity tileEntity = world.getTileEntity(sourcePosition);
      FluidTank sourceFluidTank = this.getCollectionSourceFluidTank(tileEntity);

      if (sourceFluidTank != null) {
        fluidTanks.add(sourceFluidTank);
      }
    }

    fluidTanks.sort(Comparator.comparingInt(FluidTank::getFluidAmount).reversed());

    for (FluidTank tank : fluidTanks) {

      if (fluidTank.getFluidAmount() < fluidTank.getCapacity()) {
        this.collect(tank, fluidTank);

      } else {
        break;
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

    private final TileTarTankBase tile;

    /* package */ Tank(TileTarTankBase tile, int capacity) {

      super(capacity);
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
