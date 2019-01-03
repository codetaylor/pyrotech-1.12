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

    Tank tank = new Tank();
    registrar.registerBodyProvider(tank, TileTarTankBase.class);

    KilnPit pitKilnDataProvider = new KilnPit();
    registrar.registerBodyProvider(pitKilnDataProvider, TileKilnPit.class);

    MillStone millStone = new MillStone();
    registrar.registerBodyProvider(millStone, TileMillStone.class);
    registrar.registerBodyProvider(millStone, TileMillStoneTop.class);

    CombustionWorkerStoneItemInItemOut combustionWorkerStoneItemInItemOut = new CombustionWorkerStoneItemInItemOut();
    registrar.registerBodyProvider(combustionWorkerStoneItemInItemOut, TileOvenStone.class);
    registrar.registerBodyProvider(combustionWorkerStoneItemInItemOut, TileKilnStone.class);
    registrar.registerBodyProvider(combustionWorkerStoneItemInItemOut, TileStoneTop.class);

    CombustionWorkerStoneItemInFluidOut combustionWorkerStoneItemInFluidOut = new CombustionWorkerStoneItemInFluidOut();
    registrar.registerBodyProvider(combustionWorkerStoneItemInFluidOut, TileCrucibleStone.class);
    registrar.registerBodyProvider(combustionWorkerStoneItemInFluidOut, TileStoneTop.class);

    Campfire campfire = new Campfire();
    registrar.registerBodyProvider(campfire, TileCampfire.class);

    DryingRack dryingRack = new DryingRack();
    registrar.registerBodyProvider(dryingRack, TileDryingRackBase.class);

    ChoppingBlock choppingBlock = new ChoppingBlock();
    registrar.registerBodyProvider(choppingBlock, TileChoppingBlock.class);

    WoodRack woodRack = new WoodRack();
    registrar.registerBodyProvider(woodRack, TileWoodRack.class);

    GraniteAnvil graniteAnvil = new GraniteAnvil();
    registrar.registerBodyProvider(graniteAnvil, TileGraniteAnvil.class);

    CraftingProvider craftingProvider = new CraftingProvider();
    registrar.registerBodyProvider(craftingProvider, TileCrafting.class);
  }

}
