package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.pyrotech.modules.hunting.network.CSPacketCapabilitySyncSpearRequest;
import com.codetaylor.mc.pyrotech.modules.hunting.network.SCPacketCapabilitySyncSpear;
import com.codetaylor.mc.pyrotech.modules.hunting.network.SCPacketParticleMud;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketInitializer {

  public static void register(IPacketRegistry registry) {

    registry.register(
        SCPacketCapabilitySyncSpear.class,
        SCPacketCapabilitySyncSpear.class,
        Side.CLIENT
    );

    registry.register(
        SCPacketParticleMud.class,
        SCPacketParticleMud.class,
        Side.CLIENT
    );

    registry.register(
        CSPacketCapabilitySyncSpearRequest.class,
        CSPacketCapabilitySyncSpearRequest.class,
        Side.SERVER
    );
  }

  private PacketInitializer() {
    //
  }
}
