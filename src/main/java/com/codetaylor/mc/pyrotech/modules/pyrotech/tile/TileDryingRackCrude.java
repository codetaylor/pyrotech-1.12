package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class TileDryingRackCrude
    extends TileDryingRackBase {

  public TileDryingRackCrude() {

    super();
  }

  @Override
  protected int getSlotCount() {

    return 1;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.CRUDE_DRYING_RACK.SPEED_MODIFIER);
  }

  @Override
  protected float getRainSpeed() {

    return 0;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getRenderBoundingBox() {

    return new AxisAlignedBB(this.getPos());
  }
}
