package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.pyrotech.library.fluid.SCPacketFluidUpdate;
import com.codetaylor.mc.athenaeum.network.tile.SCPacketTileData;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.CSPacketInteractionMouseWheel;
import net.minecraftforge.fml.relauncher.Side;

public final class ModulePackets {

  public static void register(IPacketRegistry registry) {

    registry.register(
        CSPacketInteractionMouseWheel.class,
        CSPacketInteractionMouseWheel.class,
        Side.SERVER
    );

    // Tile Data
    registry.register(
        SCPacketTileData.class,
        SCPacketTileData.class,
        Side.CLIENT
    );

    // TODO: Switch to tile data system
    // Fluid Update
    registry.register(
        SCPacketFluidUpdate.class,
        SCPacketFluidUpdate.class,
        Side.CLIENT
    );
  }

  private ModulePackets() {
    //
  }
}
