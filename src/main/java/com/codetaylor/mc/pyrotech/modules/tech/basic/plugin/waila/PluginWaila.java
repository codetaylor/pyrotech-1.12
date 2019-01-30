package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    KilnPitProvider pitKilnDataProvider = new KilnPitProvider();
    registrar.registerBodyProvider(pitKilnDataProvider, TileKilnPit.class);

    CampfireProvider campfireProvider = new CampfireProvider();
    registrar.registerBodyProvider(campfireProvider, TileCampfire.class);

    DryingRackProvider dryingRackProvider = new DryingRackProvider();
    registrar.registerBodyProvider(dryingRackProvider, TileDryingRackBase.class);

    ChoppingBlockProvider choppingBlockProvider = new ChoppingBlockProvider();
    registrar.registerBodyProvider(choppingBlockProvider, TileChoppingBlock.class);

    AnvilProvider anvilProvider = new AnvilProvider();
    registrar.registerBodyProvider(anvilProvider, TileAnvilBase.class);

    WorktableProvider worktableProvider = new WorktableProvider();
    registrar.registerBodyProvider(worktableProvider, TileWorktable.class);

    CompactingBinProvider compactingBinProvider = new CompactingBinProvider();
    registrar.registerBodyProvider(compactingBinProvider, TileCompactingBin.class);

    SoakingPotProvider soakingPotProvider = new SoakingPotProvider();
    registrar.registerBodyProvider(soakingPotProvider, TileSoakingPot.class);
  }

}