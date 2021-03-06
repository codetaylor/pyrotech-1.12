package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.pyrotech.modules.core.event.NoHungerEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SCPacketNoHunger
    implements IMessage,
    IMessageHandler<SCPacketNoHunger, IMessage> {

  @SuppressWarnings("unused")
  public SCPacketNoHunger() {
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
  public IMessage onMessage(SCPacketNoHunger message, MessageContext ctx) {

    MinecraftForge.EVENT_BUS.post(new NoHungerEventHandler.NoHungerEvent());
    return null;
  }
}
