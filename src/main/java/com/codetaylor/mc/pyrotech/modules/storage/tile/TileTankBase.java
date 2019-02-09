package com.codetaylor.mc.pyrotech.modules.storage.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionBounds;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetBase;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import net.minecraft.init.SoundEvents;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileTankBase
    extends TileNetBase
    implements ITileInteractable {

  private final TileDataFluidTank<Tank> tileDataFluidTank;
  private final Tank tank;
  private final IInteraction[] interactions;

  public TileTankBase() {

    super(ModuleStorage.TILE_DATA_SERVICE);

    this.tank = new Tank(this, this.getTankCapacity());
    this.tank.addObserver((fluidTank, amount) -> this.markDirty());

    // --- Network ---

    this.tileDataFluidTank = new TileDataFluidTank<>(this.tank);

    this.registerTileDataForNetwork(new ITileData[]{
        this.tileDataFluidTank
    });

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionBucket(this.tank)
    };
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public FluidTank getFluidTank() {

    return this.tank;
  }

  protected abstract int getTankCapacity();

  protected abstract boolean canHoldHotFluids();

  protected abstract int getHotFluidTemperature();

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataFluidTank.isDirty()) {
      this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Capability
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.tank;
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  protected void setWorldCreate(World world) {

    this.world = world;
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.tank.readFromNBT(compound.getCompoundTag("tank"));
    this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);

    if (this.world.isRemote) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  public class InteractionBucket
      extends InteractionBucketBase<TileTankBase> {

    private final FluidTank tank;

    /* package */ InteractionBucket(FluidTank tank) {

      super(
          tank,
          EnumFacing.VALUES,
          InteractionBounds.BLOCK
      );
      this.tank = tank;
    }

    public FluidTank getFluidTank() {

      return this.tank;
    }
  }

  // ---------------------------------------------------------------------------
  // - Tank
  // ---------------------------------------------------------------------------

  private class Tank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileTankBase tile;

    /* package */ Tank(TileTankBase tile, int capacity) {

      super(capacity);
      this.tile = tile;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      int filled = super.fill(resource, doFill);

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

    // Special serialization to bypass a bug where the ItemBlock merges an
    // empty tank with the full tank, adding the Empty tag to an otherwise
    // full tank.

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

      if (this.fluid != null) {
        this.fluid.writeToNBT(nbt);

      } else {
        nbt.setString("Empty", "");
      }
      return nbt;
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {

      if (nbt.hasKey("Empty")) {
        // We need to check if it actually is empty, because of the bug
        // mentioned above.

        if (!nbt.hasKey("Amount")
            || nbt.getInteger("Amount") <= 0) {
          this.setFluid(null);
          return this;
        }
      }

      FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
      this.setFluid(fluid);
      return this;
    }
  }
}
