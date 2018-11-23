package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class TileDataService
    implements ITileDataService {

  private final List<TileDataTracker> dataTrackerList;
  private final IPacketService packetService;

  public TileDataService(IPacketService packetService) {

    this.packetService = packetService;
    this.dataTrackerList = new ArrayList<>();
  }

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
    }
  }

  @Override
  public void update() {

    for (TileDataTracker tracker : this.dataTrackerList) {

      tracker.update(this.packetService);
    }
  }

  @SubscribeEvent
  public static void on(TickEvent.ServerTickEvent event) {

    if (event.phase == TickEvent.Phase.END) {
      // TODO: remove module dependency
      ModulePyrotech.TILE_DATA_SERVICE.update();
    }
  }

}
