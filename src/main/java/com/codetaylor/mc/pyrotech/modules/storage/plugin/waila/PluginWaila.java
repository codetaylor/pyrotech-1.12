package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.storage.tile.*;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    {
      StorageProvider provider = new StorageProvider();
      registrar.registerBodyProvider(provider, TileShelf.class);
      registrar.registerBodyProvider(provider, TileCrate.class);
      registrar.registerBodyProvider(provider, TileStash.class);
    }

    {
      WoodRackProvider provider = new WoodRackProvider();
      registrar.registerBodyProvider(provider, TileWoodRack.class);
    }

    {
      TankProvider provider = new TankProvider();
      registrar.registerBodyProvider(provider, TileTankBase.class);
    }
  }
}
