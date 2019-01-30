package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.CombustionWorkerStoneItemInFluidOutProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.CombustionWorkerStoneItemInItemOutProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.SawmillProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    {
      SawmillProvider provider = new SawmillProvider();
      registrar.registerBodyProvider(provider, TileStoneSawmill.class);
      registrar.registerBodyProvider(provider, TileStoneSawmillTop.class);
    }

    {
      CombustionWorkerStoneItemInItemOutProvider provider = new CombustionWorkerStoneItemInItemOutProvider();
      registrar.registerBodyProvider(provider, TileStoneOven.class);
      registrar.registerBodyProvider(provider, TileStoneOvenTop.class);
      registrar.registerBodyProvider(provider, TileStoneKiln.class);
      registrar.registerBodyProvider(provider, TileStoneKilnTop.class);
    }

    {
      CombustionWorkerStoneItemInFluidOutProvider provider = new CombustionWorkerStoneItemInFluidOutProvider();
      registrar.registerBodyProvider(provider, TileStoneCrucible.class);
      registrar.registerBodyProvider(provider, TileStoneCrucibleTop.class);
    }
  }

}
