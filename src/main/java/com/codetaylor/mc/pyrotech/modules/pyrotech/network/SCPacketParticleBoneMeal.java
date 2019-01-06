package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import com.codetaylor.mc.athenaeum.spi.packet.PacketBlockPosBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemDye;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketParticleBoneMeal
    extends PacketBlockPosBase<SCPacketParticleBoneMeal> {

  private int amount;

  @SuppressWarnings("unused")
  public SCPacketParticleBoneMeal() {
    // serialization
  }

  public SCPacketParticleBoneMeal(BlockPos pos, int amount) {

    super(pos);
    this.amount = amount;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);
    buf.writeInt(this.amount);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);
    this.amount = buf.readInt();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketParticleBoneMeal message, MessageContext ctx) {

    ItemDye.spawnBonemealParticles(Minecraft.getMinecraft().world, message.blockPos, message.amount);
    return null;
  }
}
