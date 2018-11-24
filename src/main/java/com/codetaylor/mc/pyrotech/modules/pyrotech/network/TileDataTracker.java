package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

class TileDataTracker {

  private final TileDataContainerBase tile;
  private final ITileData[] data;
  private final PacketBuffer packetBuffer;

  /* package */ TileDataTracker(TileDataContainerBase tile, ITileData[] data) {

    this.tile = tile;
    this.data = data;
    this.packetBuffer = new PacketBuffer(Unpooled.buffer());
  }

  public TileDataContainerBase getTile() {

    return this.tile;
  }

  /**
   * Called once per tick on the server.
   */
  /* package */ PacketBuffer getUpdateBuffer() {

    int dirtyCount = 0;

    for (int i = 0; i < this.data.length; i++) {

      if (this.data[i].isDirty()) {
        dirtyCount += 1;
      }
    }

    this.packetBuffer.clear();

    if (dirtyCount > 0) {
      this.packetBuffer.writeInt(dirtyCount);

      for (int i = 0; i < this.data.length; i++) {

        if (this.data[i].isDirty()
            && this.data[i].canUpdate()) {
          this.packetBuffer.writeInt(i);
          this.data[i].write(this.packetBuffer);
          this.data[i].setDirty(false);
        }
      }
    }

    return this.packetBuffer;
  }

  /**
   * Called when an update packet arrives on the client.
   *
   * @param buffer the update buffer
   */
  @SideOnly(Side.CLIENT)
  /* package */ void updateClient(PacketBuffer buffer) throws IOException {

    // Deserialize the buffer.

    int dirtyCount = buffer.readInt();

    for (int i = 0; i < dirtyCount; i++) {
      int index = buffer.readInt();
      this.data[index].read(buffer);
      this.data[index].setDirty(true);
    }

    // Notify the tile that the data has updated.

    for (int i = 0; i < this.data.length; i++) {

      if (this.data[i].isDirty()) {
        this.tile.onTileDataUpdate(this.data[i]);
        this.data[i].setDirty(false);
      }
    }
  }

}
