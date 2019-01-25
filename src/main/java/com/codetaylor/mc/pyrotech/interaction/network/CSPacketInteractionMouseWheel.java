package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.network;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockCombustionWorkerStoneBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketInteractionMouseWheel
    implements IMessage,
    IMessageHandler<CSPacketInteractionMouseWheel, IMessage> {

  private BlockPos origin;
  private int wheelDelta;
  private EnumFacing facing;
  private Vec3d hitVec;

  @SuppressWarnings("unused")
  public CSPacketInteractionMouseWheel() {
    // Serialization
  }

  public CSPacketInteractionMouseWheel(BlockPos origin, int wheelDelta, EnumFacing facing, Vec3d hitVec) {

    this.origin = origin;
    this.wheelDelta = wheelDelta;
    this.facing = facing;
    this.hitVec = hitVec;
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    PacketBuffer pb = new PacketBuffer(buf);
    this.origin = pb.readBlockPos();
    this.wheelDelta = pb.readInt();
    this.facing = EnumFacing.VALUES[pb.readByte()];
    this.hitVec = new Vec3d(pb.readDouble(), pb.readDouble(), pb.readDouble());
  }

  @Override
  public void toBytes(ByteBuf buf) {

    PacketBuffer pb = new PacketBuffer(buf);
    pb.writeBlockPos(this.origin);
    pb.writeInt(this.wheelDelta);
    pb.writeByte(this.facing.getIndex());
    pb.writeDouble(this.hitVec.x);
    pb.writeDouble(this.hitVec.y);
    pb.writeDouble(this.hitVec.z);
  }

  @Override
  public IMessage onMessage(CSPacketInteractionMouseWheel message, MessageContext ctx) {

    if (message.wheelDelta != 0) {

      NetHandlerPlayServer serverHandler = ctx.getServerHandler();
      EntityPlayerMP player = serverHandler.player;
      World world = player.getEntityWorld();
      BlockPos origin = message.origin;
      EnumFacing facing = message.facing;
      IBlockState blockState = world.getBlockState(origin);
      Block block = blockState.getBlock();

      if (block instanceof IBlockInteractable) {

        IInteraction.EnumType type;

        if (message.wheelDelta > 0) {
          type = IInteraction.EnumType.MouseWheelUp;

        } else {
          type = IInteraction.EnumType.MouseWheelDown;
        }

        if (block instanceof BlockCombustionWorkerStoneBase
            && ((BlockCombustionWorkerStoneBase) block).isTop(blockState)) {
          ((IBlockInteractable) block).interact(
              type,
              world,
              origin.down(),
              blockState,
              player,
              EnumHand.MAIN_HAND,
              facing,
              (float) message.hitVec.x,
              (float) message.hitVec.y + 1,
              (float) message.hitVec.z
          );
        }

        ((IBlockInteractable) block).interact(
            type,
            world,
            origin,
            blockState,
            player,
            EnumHand.MAIN_HAND,
            facing,
            (float) message.hitVec.x,
            (float) message.hitVec.y,
            (float) message.hitVec.z
        );
      }
    }

    return null;
  }
}
