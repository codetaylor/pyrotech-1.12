package com.codetaylor.mc.pyrotech.library.spi.tile;

import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataInteger;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileDataBase;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class TileCombustionWorkerBase
    extends TileNetWorkerBase {

  protected TileDataInteger burnTimeRemaining;

  private int rainTimeRemaining;

  protected TileCombustionWorkerBase(ITileDataService tileDataService, int taskCount) {

    super(tileDataService, taskCount);

    this.burnTimeRemaining = new TileDataInteger(this.combustionGetInitialBurnTimeRemaining(), 20);
    this.burnTimeRemaining.addChangeObserver(new TileDataBase.IChangeObserver.OnDirtyMarkTileDirty<>(this));

    // --- Network ---
    this.registerTileDataForNetwork(new ITileData[]{
        this.burnTimeRemaining
    });

    this.rainTimeRemaining = this.combustionGetRainDeactivateTime();
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public int combustionGetBurnTimeRemaining() {

    return this.burnTimeRemaining.get();
  }

  // ---------------------------------------------------------------------------
  // - Overrides
  // ---------------------------------------------------------------------------

  /**
   * @return the number of ticks of rain required to deactivate this worker
   * or -1 to prevent the douse behavior
   */
  protected int combustionGetRainDeactivateTime() {

    return -1;
  }

  protected int combustionGetInitialBurnTimeRemaining() {

    return 0;
  }

  protected int combustionGetBurnTimeForFuel(ItemStack fuel) {

    return StackHelper.getItemBurnTime(fuel);
  }

  /**
   * Called when this worker is deactivated by rain.
   */
  protected void combustionOnDeactivatedByRain() {
    // no-op, override
  }

  /**
   * Called to retrieve a fuel item from the subclass.
   * <p>
   * Stack handlers should go ahead and remove items during this call.
   *
   * @return a valid fuel item
   */
  protected abstract ItemStack combustionGetFuelItem();

  // ---------------------------------------------------------------------------
  // - Worker
  // ---------------------------------------------------------------------------

  @Override
  public boolean workerRequiresFuel() {

    return (this.combustionGetBurnTimeRemaining() <= 0);
  }

  @Override
  public boolean workerConsumeFuel() {

    ItemStack fuel = this.combustionGetFuelItem();

    if (!fuel.isEmpty()) {
      this.burnTimeRemaining.set(this.combustionGetBurnTimeForFuel(fuel));
      return true;
    }

    this.workerSetActive(false);

    return false;
  }

  @Override
  public boolean workerDoWork() {

    if (this.combustionGetRainDeactivateTime() > -1) {

      if (this.world.isRainingAt(this.pos.up())) {

        if (this.rainTimeRemaining > 0) {
          this.rainTimeRemaining -= 1;
        }

        if (this.rainTimeRemaining == 0) {
          this.combustionOnDeactivatedByRain();
          return false;
        }

      } else {
        this.rainTimeRemaining = this.combustionGetRainDeactivateTime();
      }

    }

    this.burnTimeRemaining.add(-1);

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setInteger("burnTimeRemaining", this.burnTimeRemaining.get());

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.burnTimeRemaining.set(compound.getInteger("burnTimeRemaining"));
  }
}
