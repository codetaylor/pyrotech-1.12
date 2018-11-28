package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.*;
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

    Tank tank = new Tank();
    registrar.registerBodyProvider(tank, TileTarTankBase.class);

    KilnPit pitKilnDataProvider = new KilnPit();
    registrar.registerBodyProvider(pitKilnDataProvider, TileKilnPit.class);

    KilnBrick kilnBrick = new KilnBrick();
    registrar.registerBodyProvider(kilnBrick, TileKilnBrick.class);
    registrar.registerBodyProvider(kilnBrick, TileKilnBrickTop.class);

    KilnStone kiLnStone = new KilnStone();
    registrar.registerBodyProvider(kiLnStone, TileKilnStone.class);
    registrar.registerBodyProvider(kiLnStone, TileKilnStoneTop.class);

    Campfire campfire = new Campfire();
    registrar.registerBodyProvider(campfire, TileCampfire.class);

    DryingRack dryingRack = new DryingRack();
    registrar.registerBodyProvider(dryingRack, TileDryingRackBase.class);

    ChoppingBlock choppingBlock = new ChoppingBlock();
    registrar.registerBodyProvider(choppingBlock, TileChoppingBlock.class);

    WoodRack woodRack = new WoodRack();
    registrar.registerBodyProvider(woodRack, TileWoodRack.class);
  }

}
