package com.codetaylor.mc.pyrotech.modules.hunting.client;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.world.World;

public class ParticleMud
    extends ParticleBreaking {

  public ParticleMud(World world, double x, double y, double z) {

    super(world, x, y, z, ModuleCore.Items.ROCK, 11);
  }
}
