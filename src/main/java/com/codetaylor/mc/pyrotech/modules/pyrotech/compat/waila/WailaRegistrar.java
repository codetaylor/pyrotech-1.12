package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.*;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegistrar {

  static final String CONFIG_TANK = Util.prefix("tank");
  static final String CONFIG_PROGRESS = Util.prefix("progress");

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    registrar.addConfig(ModulePyrotech.MOD_ID, CONFIG_TANK, true);
    registrar.addConfig(ModulePyrotech.MOD_ID, CONFIG_PROGRESS, true);

    TankDataProvider tankDataProvider = new TankDataProvider();
    registrar.registerBodyProvider(tankDataProvider, TileTarTankBase.class);

    KilnPitDataProvider pitKilnDataProvider = new KilnPitDataProvider();
    registrar.registerBodyProvider(pitKilnDataProvider, TileKilnPit.class);

    KilnBrickDataProvider kilnBrickDataProvider = new KilnBrickDataProvider();
    registrar.registerBodyProvider(kilnBrickDataProvider, TileKilnBrick.class);
    registrar.registerBodyProvider(kilnBrickDataProvider, TileKilnBrickTop.class);

    CampfireDataProvider campfireDataProvider = new CampfireDataProvider();
    registrar.registerBodyProvider(campfireDataProvider, TileCampfire.class);

    DryingRackDataProvider dryingRackDataProvider = new DryingRackDataProvider();
    registrar.registerBodyProvider(dryingRackDataProvider, TileDryingRack.class);
  }

}
