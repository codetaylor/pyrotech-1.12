package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.pyrotech.modules.tech.basic.network.SCPacketParticleAnvilHit;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketInitializer {

  public static void register(IPacketRegistry registry) {

    registry.register(
        SCPacketParticleAnvilHit.class,
        SCPacketParticleAnvilHit.class,
        Side.CLIENT
    );
  }

  private PacketInitializer() {
    //
  }
}
