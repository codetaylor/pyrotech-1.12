package com.codetaylor.mc.pyrotech.modules.tech.bloomery.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Derived from ParticleLava
 */
public class ParticleBloomeryDrip
    extends Particle {

  private final float lavaParticleScale;

  protected ParticleBloomeryDrip(World world, double x, double y, double z) {

    super(world, x, y, z);

    this.motionX *= 0.800000011920929D;
    this.motionY *= 0.800000011920929D;
    this.motionZ *= 0.800000011920929D;
    this.particleRed = 1.0F;
    this.particleGreen = 1.0F;
    this.particleBlue = 1.0F;
    this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
    this.lavaParticleScale = this.particleScale;
    this.particleMaxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
    this.setParticleTextureIndex(49);
  }

  public int getBrightnessForRender(float partialTicks) {

    int i = super.getBrightnessForRender(partialTicks);
    int j = 240;
    int k = i >> 16 & 255;
    return j | k << 16;
  }

  public void onUpdate() {

    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    if (this.particleAge++ >= this.particleMaxAge) {
      this.setExpired();
    }

    float f = (float) this.particleAge / (float) this.particleMaxAge;

    if (this.rand.nextFloat() > f) {
      this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, 0.1, this.motionZ);
    }

    this.motionY -= 0.015D;
    this.move(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.9990000128746033D;
    this.motionY *= 0.9990000128746033D;
    this.motionZ *= 0.9990000128746033D;

    if (this.onGround) {
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
    }
  }

  @Override
  public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

    float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
    this.particleScale = this.lavaParticleScale * (1.0F - f * f);
    super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
  }

  public static Particle createParticle(World world, double x, double y, double z) {

    return new ParticleBloomeryDrip(world, x, y, z);
  }

}
