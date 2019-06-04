package com.codetaylor.mc.pyrotech.library.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class ParticleFactoryAdapter
    implements IParticleFactory {

  private final net.minecraft.client.particle.IParticleFactory particleFactory;

  public ParticleFactoryAdapter(net.minecraft.client.particle.IParticleFactory particleFactory) {

    this.particleFactory = particleFactory;
  }

  @Override
  public Particle createParticle(World world, double x, double y, double z) {

    return this.particleFactory.createParticle(-1, world, x, y, z, 0, 0, 0);
  }
}
