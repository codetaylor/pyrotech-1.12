package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.library.spi.tile.TileCapabilityDelegate;
import net.minecraft.util.EnumFacing;

public class TileStoneTop
    extends TileCapabilityDelegate {

  protected TileStoneTop() {

    super(EnumFacing.DOWN, EnumFacing.UP);
  }
}
