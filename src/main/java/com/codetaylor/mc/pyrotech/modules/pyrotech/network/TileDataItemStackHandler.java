package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;

public class TileDataItemStackHandler
    extends TileDataBase {

  private ItemStackHandler value;

  public TileDataItemStackHandler(ItemStackHandler initialValue) {

    this(initialValue, 1);
  }

  public TileDataItemStackHandler(ItemStackHandler initialValue, int updateInterval) {

    super(updateInterval);
    this.setValue(initialValue);
  }

  public void setValue(ItemStackHandler value) {

    this.value = value;
    this.setDirty(true);
  }

  public ItemStackHandler getValue() {

    return this.value;
  }

  @Override
  public void read(PacketBuffer buffer) throws IOException {

    NBTTagCompound tag = buffer.readCompoundTag();

    if (tag != null) {
      this.value.deserializeNBT(tag);
    }
  }

  @Override
  public void write(PacketBuffer buffer) {

    buffer.writeCompoundTag(this.value.serializeNBT());
  }

}
