package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileDryingRackBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileTarTankBase;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegistrar {

  public static final String CONFIG_TANK = Util.prefix("tank");
  public static final String CONFIG_PROGRESS = Util.prefix("progress");
  public static final String CONFIG_CONTENTS = Util.prefix("contents");

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    registrar.addConfig(ModulePyrotech.MOD_ID, CONFIG_TANK, true);
    registrar.addConfig(ModulePyrotech.MOD_ID, CONFIG_PROGRESS, true);
    registrar.addConfig(ModulePyrotech.MOD_ID, CONFIG_CONTENTS, true);

    TankProvider tankProvider = new TankProvider();
    registrar.registerBodyProvider(tankProvider, TileTarTankBase.class);

    KilnPitProvider pitKilnDataProvider = new KilnPitProvider();
    registrar.registerBodyProvider(pitKilnDataProvider, TileKilnPit.class);

    CampfireProvider campfireProvider = new CampfireProvider();
    registrar.registerBodyProvider(campfireProvider, TileCampfire.class);

    DryingRackProvider dryingRackProvider = new DryingRackProvider();
    registrar.registerBodyProvider(dryingRackProvider, TileDryingRackBase.class);

    ChoppingBlockProvider choppingBlockProvider = new ChoppingBlockProvider();
    registrar.registerBodyProvider(choppingBlockProvider, TileChoppingBlock.class);

    GraniteAnvilProvider graniteAnvilProvider = new GraniteAnvilProvider();
    registrar.registerBodyProvider(graniteAnvilProvider, TileAnvilBase.class);

    WorktableProvider worktableProvider = new WorktableProvider();
    registrar.registerBodyProvider(worktableProvider, TileWorktable.class);

    CompactingBinProvider compactingBinProvider = new CompactingBinProvider();
    registrar.registerBodyProvider(compactingBinProvider, TileCompactingBin.class);

    SoakingPotProvider soakingPotProvider = new SoakingPotProvider();
    registrar.registerBodyProvider(soakingPotProvider, TileSoakingPot.class);
  }

}
