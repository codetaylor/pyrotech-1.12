package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.pyrotech.library.particle.ParticleEmitter;
import com.codetaylor.mc.pyrotech.library.particle.ParticleFactoryAdapter;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDrip;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketParticleDrip
    implements IMessage,
    IMessageHandler<SCPacketParticleDrip, IMessage> {

  private double x;
  private double y;
  private double z;
  private double rangeX;
  private double rangeY;
  private double rangeZ;

  @SuppressWarnings("unused")
  public SCPacketParticleDrip() {
    // serialization
  }

  public SCPacketParticleDrip(double x, double y, double z, double rangeX, double rangeY, double rangeZ) {

    this.x = x;
    this.y = y;
    this.z = z;
    this.rangeX = rangeX;
    this.rangeY = rangeY;
    this.rangeZ = rangeZ;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    buf.writeDouble(this.x);
    buf.writeDouble(this.y);
    buf.writeDouble(this.z);
    buf.writeDouble(this.rangeX);
    buf.writeDouble(this.rangeY);
    buf.writeDouble(this.rangeZ);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.x = buf.readDouble();
    this.y = buf.readDouble();
    this.z = buf.readDouble();
    this.rangeX = buf.readDouble();
    this.rangeY = buf.readDouble();
    this.rangeZ = buf.readDouble();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketParticleDrip message, MessageContext ctx) {

    if (ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES) {

      WorldClient world = Minecraft.getMinecraft().world;

      ParticleEmitter particleEmitter = new ParticleEmitter(
          world,
          new Vec3d(message.x, message.y, message.z),
          new Vec3d(message.rangeX, message.rangeY, message.rangeZ),
          2,
          30,
          new ParticleFactoryAdapter(new CustomParticleDrip.Factory())
      );

      Minecraft.getMinecraft().effectRenderer.addEffect(
          particleEmitter
      );
    }

    return null;
  }

  @SideOnly(Side.CLIENT)
  public static class CustomParticleDrip
      extends ParticleDrip {

    protected CustomParticleDrip(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_) {

      super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1203_8_);
      this.particleMaxAge = 30;
      this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 1.5F;
      this.motionY = 0;
    }

    @Override
    public void onUpdate() {

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;

      this.particleRed = 0.2F;
      this.particleGreen = 0.3F;
      this.particleBlue = 1.0F;

      this.motionY -= ((this.particleAge / (float) this.particleMaxAge) * 0.9 + 0.1) * (double) this.particleGravity * 0.5;

      this.setParticleTextureIndex(112);

      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;

      if (this.onGround || this.particleAge++ >= this.particleMaxAge) {
        this.setExpired();
        Minecraft.getMinecraft().effectRenderer.addEffect(
            new CustomParticleSplash(this.world, this.posX, this.posY, this.posZ, 0, 0, 0)
        );

        this.motionX *= 0.699999988079071D;
        this.motionZ *= 0.699999988079071D;
      }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory
        implements IParticleFactory {

      public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {

        return new CustomParticleDrip(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.WATER);
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public static class CustomParticleSplash
      extends ParticleSplash {

    public CustomParticleSplash(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {

      super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
      this.particleScale = 0.75f;
    }

    public void onUpdate() {

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= (double) this.particleGravity;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;

      if (this.onGround) {
        if (Math.random() < 0.5D) {
          this.setExpired();
        }

        this.motionX *= 0.699999988079071D;
        this.motionZ *= 0.699999988079071D;
      }
    }
  }
}
