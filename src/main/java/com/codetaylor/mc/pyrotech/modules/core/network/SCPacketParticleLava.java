package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.athenaeum.spi.packet.PacketBlockPosBase;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class SCPacketParticleLava
    extends PacketBlockPosBase<SCPacketParticleLava> {

  private int level;

  @SuppressWarnings("unused")
  public SCPacketParticleLava() {
    // serialization
  }

  public SCPacketParticleLava(BlockPos pos, int level) {

    super(pos);
    this.level = level;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);
    buf.writeInt(this.level);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);
    this.level = buf.readInt();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketParticleLava message, MessageContext ctx) {

    WorldClient world = Minecraft.getMinecraft().world;
    BlockPos pos = message.blockPos;
    Random rand = RandomHelper.random();

    double x = (double) pos.getX() + 0.5;
    double y = (double) pos.getY() + ((message.level - 1) / 16.0) + (rand.nextDouble() * 2.0 / 16.0);
    double z = (double) pos.getZ() + 0.5;

    for (int i = 0; i < 8; i++) {
      double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      double offsetY = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
    }

    for (int i = 0; i < 4; i++) {
      double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      double offsetY = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.3;
      world.spawnParticle(EnumParticleTypes.LAVA, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
    }

    return null;
  }
}
