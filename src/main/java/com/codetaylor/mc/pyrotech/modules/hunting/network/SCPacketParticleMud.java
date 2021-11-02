package com.codetaylor.mc.pyrotech.modules.hunting.network;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.client.ParticleMud;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class SCPacketParticleMud
    implements IMessage,
    IMessageHandler<SCPacketParticleMud, IMessage> {

  private double x;
  private double y;
  private double z;
  private int count;

  @SuppressWarnings("unused")
  public SCPacketParticleMud() {
    // Serialization
  }

  public SCPacketParticleMud(double x, double y, double z, int count) {

    this.x = x;
    this.y = y;
    this.z = z;
    this.count = count;
  }

  @Override
  public void fromBytes(ByteBuf buffer) {

    this.x = buffer.readDouble();
    this.y = buffer.readDouble();
    this.z = buffer.readDouble();
    this.count = buffer.readInt();
  }

  @Override
  public void toBytes(ByteBuf buffer) {

    buffer.writeDouble(this.x);
    buffer.writeDouble(this.y);
    buffer.writeDouble(this.z);
    buffer.writeInt(this.count);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketParticleMud message, MessageContext ctx) {

    Minecraft minecraft = Minecraft.getMinecraft();
    World world = minecraft.world;

    Random random = RandomHelper.random();

    for (int i = 0; i < message.count; i++) {
      float angle = random.nextFloat() * ((float) Math.PI * 2f);
      float offset = random.nextFloat() * 0.5f + 0.5f;
      double x = message.x + (double) (MathHelper.sin(angle) * (float) i * 0.5f * offset);
      double z = message.z + (double) (MathHelper.cos(angle) * (float) i * 0.5f * offset);
      ParticleBreaking particle = new ParticleMud(world, x, message.y, z);
      minecraft.effectRenderer.addEffect(particle);
    }

    return null;
  }
}
