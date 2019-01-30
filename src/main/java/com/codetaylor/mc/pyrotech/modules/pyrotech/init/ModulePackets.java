package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.tile.SCPacketTileData;
import com.codetaylor.mc.pyrotech.interaction.network.CSPacketInteractionMouseWheel;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.SCPacketParticleBoneMeal;
import net.minecraftforge.fml.relauncher.Side;

public final class ModulePackets {

  public static void register(IPacketRegistry registry) {

    registry.register(
        SCPacketParticleBoneMeal.class,
        SCPacketParticleBoneMeal.class,
        Side.CLIENT
    );

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
  }

  private ModulePackets() {
    //
  }
}
