package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.waila.providers.TankProvider;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarTankBase;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    TankProvider tankProvider = new TankProvider();
    registrar.registerBodyProvider(tankProvider, TileTarTankBase.class);
  }

}
