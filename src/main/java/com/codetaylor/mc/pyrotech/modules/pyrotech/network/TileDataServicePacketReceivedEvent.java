package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TileDataServicePacketReceivedEvent
    extends Event {

  private final BlockPos pos;
  private final int size;

  public TileDataServicePacketReceivedEvent(BlockPos pos, int size) {

    this.pos = pos;
    this.size = size;
  }

  public BlockPos getPos() {

    return this.pos;
  }

  public int getSize() {

    return this.size;
  }
}
