package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

public abstract class TileDataBase
    implements ITileData {

  private final int updateInterval;
  private int updateCounter;
  private boolean dirty;
  private boolean forceUpdate;

  protected TileDataBase(int updateInterval) {

    this.updateInterval = updateInterval;
  }

  @Override
  public void setDirty(boolean dirty) {

    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {

    return this.dirty && (this.updateCounter == 0);
  }

  @Override
  public void forceUpdate() {

    this.forceUpdate = true;
  }

  @Override
  public void update() {

    if (this.forceUpdate) {
      this.updateCounter = 0;
      this.forceUpdate = false;

    } else {
      this.updateCounter += 1;

      if (this.updateCounter >= this.updateInterval) {
        this.updateCounter = 0;
      }
    }
  }
}
