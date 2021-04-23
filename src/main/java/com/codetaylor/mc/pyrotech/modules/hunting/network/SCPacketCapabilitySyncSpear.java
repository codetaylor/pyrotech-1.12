package com.codetaylor.mc.pyrotech.modules.hunting.network;

import com.codetaylor.mc.pyrotech.modules.hunting.capability.CapabilitySpear;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.ISpearEntityData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketCapabilitySyncSpear
    implements IMessage,
    IMessageHandler<SCPacketCapabilitySyncSpear, IMessage> {

  private int entityId;
  private int count;

  @SuppressWarnings("unused")
  public SCPacketCapabilitySyncSpear() {
    // Serialization
  }

  public SCPacketCapabilitySyncSpear(int entityId, int count) {

    this.entityId = entityId;
    this.count = count;
  }

  @Override
  public void fromBytes(ByteBuf buffer) {

    this.entityId = buffer.readInt();
    this.count = buffer.readInt();
  }

  @Override
  public void toBytes(ByteBuf buffer) {

    buffer.writeInt(this.entityId);
    buffer.writeInt(this.count);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketCapabilitySyncSpear message, MessageContext ctx) {

    WorldClient world = Minecraft.getMinecraft().world;
    Entity entity = world.getEntityByID(message.entityId);

    if (entity instanceof EntityLivingBase) {
      ISpearEntityData data = CapabilitySpear.get((EntityLivingBase) entity);
      data.setItemStackCount(message.count);
      data.setSeed(this.entityId);
    }

    return null;
  }
}
