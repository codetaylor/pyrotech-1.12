package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.pyrotech.library.fluid.CPacketFluidUpdate;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.CPacketTileData;
import net.minecraftforge.fml.relauncher.Side;

public final class ModulePackets {

  public static void register(IPacketRegistry registry) {

    // Tile Data
    registry.register(
        CPacketTileData.class,
        CPacketTileData.class,
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
