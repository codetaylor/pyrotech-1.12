package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.pyrotech.library.fluid.CPacketFluidUpdate;
import com.codetaylor.mc.athenaeum.network.tile.SCPacketTileData;
import net.minecraftforge.fml.relauncher.Side;

public final class ModulePackets {

  public static void register(IPacketRegistry registry) {

    // Tile Data
    registry.register(
        SCPacketTileData.class,
        SCPacketTileData.class,
        Side.CLIENT
    );

    // TODO: Switch to tile data system
    // Fluid Update
    registry.register(
        CPacketFluidUpdate.class,
        CPacketFluidUpdate.class,
        Side.CLIENT
    );
  }

  private ModulePackets() {
    //
  }
}
