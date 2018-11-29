package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TileDataServiceClientMonitor {

  private final IntArrayList totalBytesReceivedPerSecond = new IntArrayList(11);
  private int totalBytesReceived;

  private float tickCounter;

  /**
   * Call once per tick to update the monitor.
   */
  public void update() {

    this.tickCounter += 1;

    if (this.tickCounter >= 20) {
      this.tickCounter = 0;
      this.totalBytesReceivedPerSecond.add(0, this.totalBytesReceived);
      this.totalBytesReceived = 0;

      if (this.totalBytesReceivedPerSecond.size() > 120) {
        this.totalBytesReceivedPerSecond.removeInt(this.totalBytesReceivedPerSecond.size() - 1);
      }
    }
  }

  public void receiveBytes(BlockPos pos, int size) {

    this.totalBytesReceived = size;
  }

  public int size() {

    return this.totalBytesReceivedPerSecond.size();
  }

  public int get(int index) {

    return this.totalBytesReceivedPerSecond.getInt(index);
  }

  public static class ClientPacketReceivedEvent
      extends Event {

    static final ClientPacketReceivedEvent INSTANCE = new ClientPacketReceivedEvent();

    static ClientPacketReceivedEvent getInstance(int serviceId, BlockPos pos, int size) {

      INSTANCE.serviceId = serviceId;
      INSTANCE.pos = pos;
      INSTANCE.size = size;
      return INSTANCE;
    }

    private int serviceId;
    private BlockPos pos;
    private int size;

    private ClientPacketReceivedEvent() {
      //
    }

    public int getServiceId() {

      return this.serviceId;
    }

    public BlockPos getPos() {

      return this.pos;
    }

    public int getSize() {

      return this.size;
    }
  }
}
