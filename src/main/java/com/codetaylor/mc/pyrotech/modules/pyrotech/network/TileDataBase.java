package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

public abstract class TileDataBase
    implements ITileData {

  private final int updateInterval;
  private int updateCounter;
  private boolean dirty;

  protected TileDataBase(int updateInterval) {

    this.updateInterval = updateInterval;
  }

  @Override
  public void setDirty(boolean dirty) {

    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {

    return this.dirty;
  }

  @Override
  public boolean canUpdate() {

    this.updateCounter += 1;

    if (this.updateCounter >= this.updateInterval) {
      this.updateCounter = 0;
      return true;
    }

    return false;
  }
}
