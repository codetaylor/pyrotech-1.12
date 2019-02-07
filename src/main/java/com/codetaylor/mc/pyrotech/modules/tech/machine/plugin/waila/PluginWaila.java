package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.CombustionMachineItemInFluidOutProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.CombustionMachineItemInItemOutProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.SawmillProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.StoneHopperProvider;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    {
      StoneHopperProvider provider = new StoneHopperProvider();
      registrar.registerBodyProvider(provider, TileStoneHopper.class);
    }

    {
      SawmillProvider provider = new SawmillProvider();
      registrar.registerBodyProvider(provider, TileStoneSawmill.class);
      registrar.registerBodyProvider(provider, TileStoneSawmillTop.class);

      registrar.registerBodyProvider(provider, TileBrickSawmill.class);
      registrar.registerBodyProvider(provider, TileBrickSawmillTop.class);
    }

    {
      CombustionMachineItemInItemOutProvider provider = new CombustionMachineItemInItemOutProvider();
      registrar.registerBodyProvider(provider, TileStoneOven.class);
      registrar.registerBodyProvider(provider, TileStoneOvenTop.class);
      registrar.registerBodyProvider(provider, TileStoneKiln.class);
      registrar.registerBodyProvider(provider, TileStoneKilnTop.class);

      registrar.registerBodyProvider(provider, TileBrickOven.class);
      registrar.registerBodyProvider(provider, TileBrickOvenTop.class);
      registrar.registerBodyProvider(provider, TileBrickKiln.class);
      registrar.registerBodyProvider(provider, TileBrickKilnTop.class);
    }

    {
      CombustionMachineItemInFluidOutProvider provider = new CombustionMachineItemInFluidOutProvider();
      registrar.registerBodyProvider(provider, TileStoneCrucible.class);
      registrar.registerBodyProvider(provider, TileStoneCrucibleTop.class);

      registrar.registerBodyProvider(provider, TileBrickCrucible.class);
      registrar.registerBodyProvider(provider, TileBrickCrucibleTop.class);
    }
  }

}
