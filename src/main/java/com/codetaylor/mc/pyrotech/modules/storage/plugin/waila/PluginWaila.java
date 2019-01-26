package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.storage.tile.TileCrate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileShelf;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    StorageProvider storageProvider = new StorageProvider();
    registrar.registerBodyProvider(storageProvider, TileShelf.class);
    registrar.registerBodyProvider(storageProvider, TileCrate.class);
    registrar.registerBodyProvider(storageProvider, TileStash.class);
  }

}
