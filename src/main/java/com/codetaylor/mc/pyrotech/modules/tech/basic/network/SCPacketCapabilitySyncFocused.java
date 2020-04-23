package com.codetaylor.mc.pyrotech.modules.tech.basic.network;

import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.CapabilityFocused;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.IFocusedPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketCapabilitySyncFocused
    implements IMessage,
    IMessageHandler<SCPacketCapabilitySyncFocused, SCPacketCapabilitySyncFocused> {

  private double remainingBonus;

  @SuppressWarnings("unused")
  public SCPacketCapabilitySyncFocused() {
    // serialization
  }

  public SCPacketCapabilitySyncFocused(IFocusedPlayerData data) {

    this.remainingBonus = data.getRemainingBonus();
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.remainingBonus = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {

    buf.writeDouble(this.remainingBonus);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public SCPacketCapabilitySyncFocused onMessage(SCPacketCapabilitySyncFocused message, MessageContext ctx) {

    CapabilityFocused.get(Minecraft.getMinecraft().player).setRemainingBonus(message.remainingBonus);
    return null;
  }
}
