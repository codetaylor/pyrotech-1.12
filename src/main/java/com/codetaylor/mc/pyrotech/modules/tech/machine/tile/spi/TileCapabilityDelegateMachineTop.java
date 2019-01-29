package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.pyrotech.library.spi.tile.TileCapabilityDelegate;
import net.minecraft.util.EnumFacing;

public abstract class TileCapabilityDelegateMachineTop
    extends TileCapabilityDelegate {

  protected TileCapabilityDelegateMachineTop() {

    super(EnumFacing.DOWN, EnumFacing.UP);
  }
}
