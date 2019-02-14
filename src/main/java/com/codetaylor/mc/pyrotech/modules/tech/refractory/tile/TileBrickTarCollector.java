package com.codetaylor.mc.pyrotech.modules.tech.refractory.tile;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarCollectorBase;

public class TileBrickTarCollector
    extends TileTarCollectorBase {

  @Override
  protected int getHotFluidTemperature() {

    return ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.HOT_TEMPERATURE;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected int getTankCapacity() {

    return ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.CAPACITY;
  }

  @Override
  protected int getSmokeParticlesPerTick() {

    return ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.SMOKE_PARTICLES_PER_TICK;
  }
}
