package com.codetaylor.mc.pyrotech.modules.core.network;

import com.codetaylor.mc.pyrotech.ModPyrotechConfig;
import com.codetaylor.mc.pyrotech.library.config.ConfigSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CSPacketModuleListResponse
    implements IMessage,
    IMessageHandler<CSPacketModuleListResponse, IMessage> {

  private static final Logger LOGGER = LogManager.getLogger(CSPacketModuleListResponse.class);

  private Map<String, Boolean> map;

  @SuppressWarnings("unused")
  public CSPacketModuleListResponse() {
    // Serialization
  }

  @Override
  public void fromBytes(ByteBuf buffer) {

    int size = buffer.readInt();
    byte[] array = new byte[size];
    buffer.readBytes(array);

    this.map = ConfigSerializer.INSTANCE.deserializeObject(array, LOGGER);
  }

  @Override
  public void toBytes(ByteBuf buffer) {

    byte[] bytes = ConfigSerializer.INSTANCE.serializeObject(ModPyrotechConfig.MODULES, LOGGER);
    buffer.writeInt(bytes.length);
    buffer.writeBytes(bytes);
  }

  @Override
  public IMessage onMessage(CSPacketModuleListResponse message, MessageContext ctx) {

    for (Map.Entry<String, Boolean> entry : ModPyrotechConfig.MODULES.entrySet()) {
      Boolean clientValue = message.map.get(entry.getKey());

      if (clientValue == null
          || clientValue != (boolean) entry.getValue()) {
        ctx.getServerHandler().disconnect(new TextComponentTranslation("gui.pyrotech.disconnect.module.mismatch", ModPyrotechConfig.MODULES.toString()));
        return null;
      }
    }

    return null;
  }
}
