package com.codetaylor.mc.pyrotech.modules.hunting.network;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.CapabilitySpear;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.ISpearEntityData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CSPacketCapabilitySyncSpearRequest
    implements IMessage,
    IMessageHandler<CSPacketCapabilitySyncSpearRequest, IMessage> {

  private int entityId;

  @SuppressWarnings("unused")
  public CSPacketCapabilitySyncSpearRequest() {
    // Serialization
  }

  public CSPacketCapabilitySyncSpearRequest(int entityId) {

    this.entityId = entityId;
  }

  @Override
  public void fromBytes(ByteBuf buffer) {

    this.entityId = buffer.readInt();
  }

  @Override
  public void toBytes(ByteBuf buffer) {

    buffer.writeInt(this.entityId);
  }

  @SideOnly(Side.SERVER)
  @Override
  public IMessage onMessage(CSPacketCapabilitySyncSpearRequest message, MessageContext ctx) {

    NetHandlerPlayServer serverHandler = ctx.getServerHandler();
    World world = serverHandler.player.world;
    Entity entity = world.getEntityByID(message.entityId);

    if (entity instanceof EntityLivingBase) {
      ISpearEntityData data = CapabilitySpear.get((EntityLivingBase) entity);

      if (data != null && data.getItemStackCount() > 0) {
        ModuleHunting.PACKET_SERVICE.sendTo(new SCPacketCapabilitySyncSpear(entity.getEntityId(), data.getItemStackCount()), serverHandler.player);
      }
    }

    return null;
  }
}
