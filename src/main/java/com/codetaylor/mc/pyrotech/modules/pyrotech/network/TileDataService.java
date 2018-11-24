package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import com.codetaylor.mc.athenaeum.network.IPacketService;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class TileDataService
    implements ITileDataService {

  private final int serviceId;
  private final IPacketService packetService;

  private final List<TileDataTracker> dataTrackerList;
  private final Map<TileDataContainerBase, TileDataTracker> dataTrackerMap;

  public TileDataService(int serviceId, IPacketService packetService) {

    this.serviceId = serviceId;
    this.packetService = packetService;
    this.dataTrackerList = new ArrayList<>();
    this.dataTrackerMap = new IdentityHashMap<>();
  }

  @Override
  public int getServiceId() {

    return this.serviceId;
  }

  @Override
  @Nullable
  public TileDataTracker getTracker(TileDataContainerBase tile) {

    return dataTrackerMap.get(tile);
  }

  @Override
  public void register(TileDataContainerBase tile) {

    Field[] declaredFields = tile.getClass().getDeclaredFields();
    List<ITileData> dataList = new ArrayList<>(declaredFields.length);

    for (int i = 0; i < declaredFields.length; i++) {

      Class<?> type = declaredFields[i].getType();

      if (ITileData.class.isAssignableFrom(type)) {
        declaredFields[i].setAccessible(true);

        try {
          dataList.add((ITileData) declaredFields[i].get(tile));

        } catch (Exception e) {

          // TODO: logger
          e.printStackTrace();
        }
      }
    }

    int size = dataList.size();

    if (size > 0) {
      ITileData[] array = new ITileData[size];
      TileDataTracker tracker = new TileDataTracker(tile, dataList.toArray(array));
      this.dataTrackerList.add(tracker);
      this.dataTrackerMap.put(tile, tracker);
    }
  }

  @Override
  public void update() {

    for (int i = 0; i < this.dataTrackerList.size(); i++) {

      // --- Bookkeeping ---

      TileDataTracker tracker = this.dataTrackerList.get(i);
      TileDataContainerBase tile = tracker.getTile();

      if (tile.isInvalid()) {
        // Move the last element to this position, remove the last element,
        // decrement the iteration index.
        this.dataTrackerList.set(i, this.dataTrackerList.get(this.dataTrackerList.size() - 1));
        this.dataTrackerList.remove(this.dataTrackerList.size() - 1);
        i -= 1;
        continue;
      }

      // --- Update Packet ---

      PacketBuffer updateBuffer = tracker.getUpdateBuffer();

      if (updateBuffer.writerIndex() > 0) {
        BlockPos tilePos = tile.getPos();
        CPacketTileData packet = new CPacketTileData(this.serviceId, tilePos, updateBuffer);
        this.packetService.sendToAllAround(packet, tile);
      }
    }
  }

}
