package com.codetaylor.mc.pyrotech.library.config;

import com.codetaylor.mc.athenaeum.util.PacketHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SCPacketConfigUpdate
    implements IMessage,
    IMessageHandler<SCPacketConfigUpdate, IMessage> {

  private static final Logger LOGGER = LogManager.getLogger(SCPacketConfigUpdate.class);

  private Class<?>[] configClasses;
  private Map<String, Map<String, byte[]>> serializedConfigs;

  @SuppressWarnings("unused")
  public SCPacketConfigUpdate() {
    // Serialization
  }

  public SCPacketConfigUpdate(Class<?>[] configClasses) {

    this.configClasses = configClasses;
  }

  @Override
  public void fromBytes(ByteBuf buffer) {

    this.serializedConfigs = new HashMap<>();

    int configCount = buffer.readInt();

    for (int i = 0; i < configCount; i++) {
      String className = PacketHelper.readString(buffer);
      int fieldCount = buffer.readInt();
      Map<String, byte[]> fieldMap = new HashMap<>();

      for (int j = 0; j < fieldCount; j++) {
        String fieldName = PacketHelper.readString(buffer);
        int payloadLength = buffer.readInt();
        byte[] payload = buffer.readBytes(payloadLength).array();
        fieldMap.put(fieldName, payload);
      }

      this.serializedConfigs.put(className, fieldMap);
    }
  }

  @Override
  public void toBytes(ByteBuf buffer) {

    this.serializedConfigs = ConfigSerializer.INSTANCE.serialize(this.configClasses, LOGGER);

    buffer.writeInt(this.serializedConfigs.size());

    for (Map.Entry<String, Map<String, byte[]>> entry : this.serializedConfigs.entrySet()) {

      String className = entry.getKey();
      Map<String, byte[]> fieldMap = entry.getValue();

      // serialize the class

      // string - class id
      // int - field count
      // [
      //   string - field name
      //   int - field byte count
      //   byte[] - field bytes
      // ]

      PacketHelper.writeString(className, buffer);
      buffer.writeInt(fieldMap.size());

      for (Map.Entry<String, byte[]> fieldEntry : fieldMap.entrySet()) {
        PacketHelper.writeString(fieldEntry.getKey(), buffer);
        buffer.writeInt(fieldEntry.getValue().length);
        buffer.writeBytes(fieldEntry.getValue());
      }
    }
  }

  @Override
  public IMessage onMessage(SCPacketConfigUpdate message, MessageContext ctx) {

    ConfigSerializer.INSTANCE.deserialize(message.serializedConfigs, LOGGER);
    return null;
  }
}
