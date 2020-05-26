package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.athenaeum.util.ParticleHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketParticleProgress
    implements IMessage,
    IMessageHandler<SCPacketParticleProgress, IMessage> {

  private int amount;
  private double x, y, z;
  private double rangeX, rangeY, rangeZ;

  @SuppressWarnings("unused")
  public SCPacketParticleProgress() {
    // serialization
  }

  public SCPacketParticleProgress(double x, double y, double z, int amount) {

    this(x, y, z, 0.5, 0.15, 0.5, amount);
  }

  public SCPacketParticleProgress(double x, double y, double z, double rangeX, double rangeY, double rangeZ, int amount) {

    this.rangeX = rangeX;
    this.rangeY = rangeY;
    this.rangeZ = rangeZ;

    this.amount = amount;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    buf.writeInt(this.amount);
    buf.writeDouble(this.x);
    buf.writeDouble(this.y);
    buf.writeDouble(this.z);
    buf.writeDouble(this.rangeX);
    buf.writeDouble(this.rangeY);
    buf.writeDouble(this.rangeZ);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.amount = buf.readInt();
    this.x = buf.readDouble();
    this.y = buf.readDouble();
    this.z = buf.readDouble();
    this.rangeX = buf.readDouble();
    this.rangeY = buf.readDouble();
    this.rangeZ = buf.readDouble();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketParticleProgress message, MessageContext ctx) {

    if (ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES) {

      if (message.amount == 0) {
        message.amount = 15;
      }

      ParticleHelper.spawnProgressParticlesClient(message.amount, message.x, message.y, message.z, message.rangeX, message.rangeY, message.rangeZ);
    }

    return null;
  }
}
