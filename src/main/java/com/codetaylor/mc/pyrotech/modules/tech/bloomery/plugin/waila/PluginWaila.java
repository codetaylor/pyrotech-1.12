package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.provider.BloomProvider;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.provider.BloomeryProvider;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    BloomeryProvider bloomeryProvider = new BloomeryProvider();
    registrar.registerBodyProvider(bloomeryProvider, TileBloomery.class);
    registrar.registerBodyProvider(bloomeryProvider, TileBloomery.Top.class);

    BloomProvider bloomProvider = new BloomProvider();
    registrar.registerBodyProvider(bloomProvider, TileBloom.class);
  }

}
