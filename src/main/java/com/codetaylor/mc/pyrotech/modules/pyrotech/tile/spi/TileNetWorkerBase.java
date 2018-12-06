package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataBoolean;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileDataBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

/**
 * A base implementation of {@link ITileWorker}.
 */
public abstract class TileNetWorkerBase
    extends TileNetBase
    implements ITileWorker,
    ITickable {

  /**
   * Active flag, synchronized with the client using the tile data service.
   */
  protected final TileDataBoolean active;

  /**
   * A float [0,1] indicating the worker's current work progress.
   */
  protected final TileDataFloat progress;

  protected TileNetWorkerBase(ITileDataService tileDataService) {

    super(tileDataService);

    this.active = new TileDataBoolean(false);
    this.active.addChangeObserver(new TileDataBase.IChangeObserver.OnDirtyMarkTileDirty(this));

    this.progress = new TileDataFloat(0, 20);
    this.progress.addChangeObserver(new TileDataBase.IChangeObserver.OnDirtyMarkTileDirty(this));

    this.registerTileData(new ITileData[]{
        this.active,
        this.progress
    });
  }

  @Override
  public boolean workerIsActive() {

    return this.active.get();
  }

  @Override
  public void workerSetActive(boolean active) {

    this.active.set(active);
  }

  @Override
  public float workerGetProgress() {

    return this.progress.get();
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    // If the worker is not active, run the inactive update method.
    if (!this.workerIsActive()) {

      if (this.workerUpdateInactive()) {
        this.workerSetActive(true);

      } else {
        return;
      }
    }

    // Try to consume fuel if the worker needs it.
    if (this.workerRequiresFuel()) {

      if (!this.workerConsumeFuel()) {
        this.workerSetActive(false);
        return;
      }
    }

    // Do the work.
    if (!this.workerDoWork()) {
      this.workerSetActive(false);
    }

    this.progress.set(this.workerCalculateProgress());
  }

  // ---------------------------------------------------------------------------
  // - Interface
  // ---------------------------------------------------------------------------

  /**
   * Called during this tile's update if the worker is not active.
   *
   * @return true if the worker should activate, false otherwise
   */
  protected boolean workerUpdateInactive() {

    return false;
  }

  /**
   * Called during this tile's update.
   *
   * @return this worker's current progress, [0,1]
   */
  protected float workerCalculateProgress() {

    return 0;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.active.set(compound.getBoolean("active"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setBoolean("active", this.active.get());

    return compound;
  }
}
