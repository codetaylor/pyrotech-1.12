package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.storage.tile.TileCrate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileShelf;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileWoodRack;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
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
      BagProvider provider = new BagProvider();
      registrar.registerBodyProvider(provider, TileBagBase.class);
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
