package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public interface ITileData {

  boolean isDirty();

  void setDirty(boolean dirty);

  boolean canUpdate();

  void read(PacketBuffer buffer) throws IOException;

  void write(PacketBuffer buffer);
}
