package com.codetaylor.mc.pyrotech.library.particle;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleEmitter
    extends Particle {

  private IParticleFactory particleFactory;
  private final Vec3d range;
  private TickCounter emitterTickInterval;

  public ParticleEmitter(World world, Vec3d pos, Vec3d range, int emitterTickInterval, int maxAge, IParticleFactory particleFactory) {

    super(world, pos.x, pos.y, pos.z);

    this.range = range;
    this.emitterTickInterval = new TickCounter(emitterTickInterval);
    this.particleFactory = particleFactory;
    this.particleMaxAge = maxAge;
  }

  @Override
  public void move(double x, double y, double z) {

    //
  }

  @Override
  public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

    //
  }

  @Override
  public int getBrightnessForRender(float p_189214_1_) {

    return 0;
  }

  @Override
  public void onUpdate() {

    if (this.emitterTickInterval.increment()
        || this.particleAge == 0) {
      this.emitParticle();
    }

    if (this.particleAge++ >= this.particleMaxAge) {
      this.setExpired();
    }
  }

  private void emitParticle() {

    double x = (RandomHelper.random().nextFloat() * 2 - 1) * this.range.x + this.posX;
    double y = (RandomHelper.random().nextFloat() * 2 - 1) * this.range.y + this.posY;
    double z = (RandomHelper.random().nextFloat() * 2 - 1) * this.range.z + this.posZ;

    Minecraft.getMinecraft().effectRenderer.addEffect(
        this.particleFactory.createParticle(this.world, x, y, z)
    );
  }
}
