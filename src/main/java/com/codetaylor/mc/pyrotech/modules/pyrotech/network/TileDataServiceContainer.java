package com.codetaylor.mc.pyrotech.modules.pyrotech.network;

import com.codetaylor.mc.athenaeum.network.IPacketService;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public final class TileDataServiceContainer {

  private static final Map<ResourceLocation, ITileDataService> SERVICE_MAP;
  private static final Int2ObjectOpenHashMap<ITileDataService> SERVICE_ID_MAP;
  private static int NEXT_ID;

  static {
    SERVICE_MAP = new HashMap<>();
    SERVICE_ID_MAP = new Int2ObjectOpenHashMap<>();
  }

  public static ITileDataService register(ResourceLocation location, IPacketService packetService) {

    if (SERVICE_MAP.get(location) != null) {
      throw new IllegalStateException("Tile data service already registered for id: " + location);
    }

    TileDataService service = new TileDataService(NEXT_ID, packetService);
    SERVICE_MAP.put(location, service);
    SERVICE_ID_MAP.put(NEXT_ID, service);

    NEXT_ID += 1;

    return service;
  }

  @Nullable
  public static ITileDataService find(ResourceLocation location) {

    return SERVICE_MAP.get(location);
  }

  @Nullable
  public static ITileDataService find(int serviceId) {

    return SERVICE_ID_MAP.get(serviceId);
  }

  public static Collection<ITileDataService> getServices() {

    return Collections.unmodifiableCollection(SERVICE_MAP.values());
  }

  @SubscribeEvent
  public static void on(TickEvent.ServerTickEvent event) {

    if (event.phase == TickEvent.Phase.END) {

      for (ITileDataService service : TileDataServiceContainer.getServices()) {
        service.update();
      }
    }
  }

}
