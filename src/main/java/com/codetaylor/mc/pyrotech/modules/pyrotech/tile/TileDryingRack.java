package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class TileDryingRack
    extends TileDryingRackBase {

  public TileDryingRack() {

    super();
  }

  @Override
  protected int getSlotCount() {

    return 4;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.DRYING_RACK.SPEED_MODIFIER);
  }
}
