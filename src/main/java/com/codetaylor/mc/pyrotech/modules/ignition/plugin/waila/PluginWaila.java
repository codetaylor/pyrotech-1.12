package com.codetaylor.mc.pyrotech.modules.ignition.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.ignition.plugin.waila.provider.OilLampProvider;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.TileLampOil;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    {
      OilLampProvider provider = new OilLampProvider();
      registrar.registerBodyProvider(provider, TileLampOil.class);
    }
  }
}
