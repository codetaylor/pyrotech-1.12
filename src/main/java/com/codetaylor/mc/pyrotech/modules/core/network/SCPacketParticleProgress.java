package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

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

      WorldClient world = Minecraft.getMinecraft().world;
      Random random = RandomHelper.random();

      for (int i = 0; i < message.amount; ++i) {
        double d0 = random.nextGaussian() * 0.02D;
        double d1 = random.nextGaussian() * 0.02D;
        double d2 = random.nextGaussian() * 0.02D;
        world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
            message.x + (random.nextFloat() * 2 - 1) * message.rangeX,
            message.y + (random.nextFloat() * 2 - 1) * message.rangeY,
            message.z + (random.nextFloat() * 2 - 1) * message.rangeZ,
            d0, d1, d2
        );
      }
    }

    return null;
  }
}
