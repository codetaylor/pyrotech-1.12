package com.codetaylor.mc.pyrotech.library.fluid;

import com.codetaylor.mc.athenaeum.spi.packet.CPacketTileEntityBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SCPacketFluidUpdate
    extends CPacketTileEntityBase<SCPacketFluidUpdate> {

  private FluidStack fluidStack;

  @SuppressWarnings("unused")
  public SCPacketFluidUpdate() {
    // serialization
  }

  public SCPacketFluidUpdate(BlockPos blockPos, FluidStack fluidStack) {

    super(blockPos);
    this.fluidStack = fluidStack;
  }

  public FluidStack getFluidStack() {

    return this.fluidStack;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);
    NBTTagCompound compound = new NBTTagCompound();

    if (this.fluidStack != null) {
      this.fluidStack.writeToNBT(compound);
    }

    ByteBufUtils.writeTag(buf, compound);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);
    NBTTagCompound compound = ByteBufUtils.readTag(buf);
    this.fluidStack = FluidStack.loadFluidStackFromNBT(compound);
  }

  @Override
  protected IMessage onMessage(SCPacketFluidUpdate message, MessageContext ctx, TileEntity tileEntity) {

    if (tileEntity instanceof IFluidUpdatePacketConsumer) {
      ((IFluidUpdatePacketConsumer) tileEntity).onFluidUpdatePacket(message);
    }

    return null;
  }

  public interface IFluidUpdatePacketConsumer {

    void onFluidUpdatePacket(SCPacketFluidUpdate packet);
  }
}
