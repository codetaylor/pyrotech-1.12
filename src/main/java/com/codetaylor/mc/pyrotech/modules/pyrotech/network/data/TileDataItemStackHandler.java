package com.codetaylor.mc.pyrotech.modules.pyrotech.network.data;

import com.codetaylor.mc.pyrotech.modules.pyrotech.network.ITileDataItemStackHandler;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.TileDataBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;

public class TileDataItemStackHandler<H extends ItemStackHandler & ITileDataItemStackHandler>
    extends TileDataBase {

  private H stackHandler;

  public TileDataItemStackHandler(H stackHandler) {

    this(stackHandler, 1);
  }

  public TileDataItemStackHandler(H stackHandler, int updateInterval) {

    super(updateInterval);
    this.stackHandler = stackHandler;
    this.stackHandler.addObserver((handler, slot) -> this.setDirty(true));
    this.setDirty(true);
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  @Override
  public void read(PacketBuffer buffer) throws IOException {

    NBTTagCompound tag = buffer.readCompoundTag();

    if (tag != null) {
      this.stackHandler.deserializeNBT(tag);
    }
  }

  @Override
  public void write(PacketBuffer buffer) {

    buffer.writeCompoundTag(this.stackHandler.serializeNBT());
  }

}
