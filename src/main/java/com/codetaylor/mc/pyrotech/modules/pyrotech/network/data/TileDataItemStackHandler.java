package com.codetaylor.mc.pyrotech.modules.pyrotech.network.data;

import com.codetaylor.mc.pyrotech.modules.pyrotech.network.ITileDataItemStackHandler;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.TileDataBase;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;
import java.util.BitSet;

public class TileDataItemStackHandler<H extends ItemStackHandler & ITileDataItemStackHandler>
    extends TileDataBase {

  private H stackHandler;
  private BitSet dirtySlots;

  public TileDataItemStackHandler(H stackHandler) {

    this(stackHandler, 1);
    this.dirtySlots = new BitSet(stackHandler.getSlots());
  }

  public TileDataItemStackHandler(H stackHandler, int updateInterval) {

    super(updateInterval);
    this.stackHandler = stackHandler;
    this.stackHandler.addObserver((handler, slot) -> {
      this.setDirty(true);
      this.dirtySlots.set(slot);
    });
    this.setDirty(true);
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  @Override
  public void setDirty(boolean dirty) {

    super.setDirty(dirty);

    if (!dirty) {
      this.dirtySlots.clear();
    }
  }

  @Override
  public void read(PacketBuffer buffer) throws IOException {

    int dirtyCount = buffer.readInt();

    for (int i = 0; i < dirtyCount; i++) {
      int slot = buffer.readInt();
      this.stackHandler.setStackInSlot(slot, new ItemStack(Preconditions.checkNotNull(buffer.readCompoundTag())));
    }
  }

  @Override
  public void write(PacketBuffer buffer) {

    final int dirtyCount = this.dirtySlots.cardinality();

    buffer.writeInt(dirtyCount);

    if (dirtyCount > 0) {

      for (int i = this.dirtySlots.nextSetBit(0); i >= 0; i = this.dirtySlots.nextSetBit(i + 1)) {
        buffer.writeInt(i);
        buffer.writeCompoundTag(this.stackHandler.getStackInSlot(i).serializeNBT());
      }
    }
  }

}
