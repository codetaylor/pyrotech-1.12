package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import net.minecraft.network.PacketBuffer;

public class TileDataBoolean
    extends TileDataBase {

  private boolean value;

  public TileDataBoolean(boolean initialValue) {

    this(initialValue, 1);
  }

  public TileDataBoolean(boolean initialValue, int updateInterval) {

    super(updateInterval);
    this.setValue(initialValue);
  }

  public void setValue(boolean value) {

    this.value = value;
    this.setDirty(true);
  }

  public boolean getValue() {

    return this.value;
  }

  @Override
  public void read(PacketBuffer buffer) {

    this.value = buffer.readBoolean();
  }

  @Override
  public void write(PacketBuffer buffer) {

    buffer.writeBoolean(this.value);
  }

}
