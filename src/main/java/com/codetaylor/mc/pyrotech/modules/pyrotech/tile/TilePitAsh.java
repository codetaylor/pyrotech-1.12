package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TilePitAsh
    extends TileEntity {

  private ItemStackHandler stackHandler;

  public TilePitAsh() {

    this.stackHandler = new ItemStackHandler(9);
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public void insertItem(ItemStack output) {

    for (int i = 0; i < 9 && !output.isEmpty(); i++) {
      output = this.stackHandler.insertItem(i, output, false);
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
  }
}
