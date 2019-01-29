package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.network.SCPacketParticleLava;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketInitializer {

  public static void register(IPacketRegistry registry) {

    registry.register(
        SCPacketParticleLava.class,
        SCPacketParticleLava.class,
        Side.CLIENT
    );
  }

  private PacketInitializer() {
    //
  }
}
