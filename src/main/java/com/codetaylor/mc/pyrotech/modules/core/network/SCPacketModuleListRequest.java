package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SCPacketModuleListRequest
    implements IMessage,
    IMessageHandler<SCPacketModuleListRequest, IMessage> {

  @SuppressWarnings("unused")
  public SCPacketModuleListRequest() {
    // Serialization
  }

  @Override
  public void fromBytes(ByteBuf buffer) {
    //
  }

  @Override
  public void toBytes(ByteBuf buffer) {
    //
  }

  @Override
  public IMessage onMessage(SCPacketModuleListRequest message, MessageContext ctx) {

    ModuleCore.PACKET_SERVICE.sendToServer(new CSPacketModuleListResponse());
    return null;
  }
}
