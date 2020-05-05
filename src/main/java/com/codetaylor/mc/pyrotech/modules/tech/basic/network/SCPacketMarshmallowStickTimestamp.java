package com.codetaylor.mc.pyrotech.modules.tech.basic.network;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemMarshmallowStick;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class SCPacketMarshmallowStickTimestamp
    implements IMessage,
    IMessageHandler<SCPacketMarshmallowStickTimestamp, SCPacketMarshmallowStickTimestamp> {

  private long[] uuid = new long[2];
  private long timestamp;

  @SuppressWarnings("unused")
  public SCPacketMarshmallowStickTimestamp() {
    // serialization
  }

  public SCPacketMarshmallowStickTimestamp(EntityPlayer player, long timestamp) {

    UUID uuid = player.getUniqueID();
    this.uuid[0] = uuid.getLeastSignificantBits();
    this.uuid[1] = uuid.getMostSignificantBits();
    this.timestamp = timestamp;
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.uuid[0] = buf.readLong();
    this.uuid[1] = buf.readLong();
    this.timestamp = buf.readLong();
  }

  @Override
  public void toBytes(ByteBuf buf) {

    buf.writeLong(this.uuid[0]);
    buf.writeLong(this.uuid[1]);
    buf.writeLong(this.timestamp);
  }

  @Override
  public SCPacketMarshmallowStickTimestamp onMessage(SCPacketMarshmallowStickTimestamp message, MessageContext ctx) {

    UUID uuid = new UUID(message.uuid[1], message.uuid[0]);
    WorldClient world = Minecraft.getMinecraft().world;

    for (EntityPlayer player : world.playerEntities) {
      UUID playerUuid = player.getUniqueID();

      if (playerUuid.equals(uuid)) {
        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        Item item = heldItemMainhand.getItem();

        System.out.println("Found player: " + playerUuid);
        System.out.println(heldItemMainhand.toString());

        if (item == ModuleTechBasic.Items.MARSHMALLOW_STICK
            && ItemMarshmallowStick.getType(heldItemMainhand) == ItemMarshmallowStick.EnumType.MARSHMALLOW) {
          ItemMarshmallowStick.setRoastByTimestamp(world, player, heldItemMainhand, message.timestamp);
        }
      }
    }

    return null;
  }
}
