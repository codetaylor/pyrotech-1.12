package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TileDataTracker {

  private final TileDataContainerBase tile;
  private final ITileData[] data;
  private final PacketBuffer packetBuffer;

  /**
   * Temporarily stores data entries to
   */
  private final List<ITileData> toUpdate;

  /* package */ TileDataTracker(TileDataContainerBase tile, ITileData[] data) {

    this.tile = tile;
    this.data = data;
    this.packetBuffer = new PacketBuffer(Unpooled.buffer());
    this.toUpdate = new ArrayList<>(this.data.length);
  }

  public TileDataContainerBase getTile() {

    return this.tile;
  }

  /**
   * Called once per tick on the server.
   * <p>
   * Returns a packet buffer containing the serialized bytes of only the data
   * that has updated. If no data has updated, an empty buffer is returned.
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

    int dirtyCount = buffer.readInt();

    // Deserialize buffer and stash updated entries.
    for (int i = 0; i < dirtyCount; i++) {
      int index = buffer.readInt();
      this.data[index].read(buffer);
      this.toUpdate.add(this.data[index]);
    }

    // Notify the tile that data was updated.
    // Clear the stash.
    if (!this.toUpdate.isEmpty()) {
      this.tile.onTileDataUpdate(Collections.unmodifiableList(this.toUpdate));
      this.toUpdate.clear();
    }
  }

}
