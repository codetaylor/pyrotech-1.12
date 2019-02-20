package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockMechanicalMulchSpreader;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    {
      CogWorkerProvider provider = new CogWorkerProvider();
      registrar.registerBodyProvider(provider, TileStoneHopper.class);
      registrar.registerBodyProvider(provider, TileMechanicalBellowsTop.class);
    }

    {
      MechanicalMulchSpreaderProvider provider = new MechanicalMulchSpreaderProvider();
      registrar.registerBodyProvider(provider, BlockMechanicalMulchSpreader.class);
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

    {
      MechanicalCompactingBinWorkerProvider provider = new MechanicalCompactingBinWorkerProvider();
      registrar.registerBodyProvider(provider, TileMechanicalCompactingBinWorker.class);
    }

    if (!ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
      MechanicalCompactingBinProvider provider = new MechanicalCompactingBinProvider();
      registrar.registerBodyProvider(provider, TileMechanicalCompactingBin.class);
    }
  }

}
