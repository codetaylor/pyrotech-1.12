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

    MillStoneProvider millStoneProvider = new MillStoneProvider();
    registrar.registerBodyProvider(millStoneProvider, TileMillStone.class);
    registrar.registerBodyProvider(millStoneProvider, TileMillStoneTop.class);

    CombustionWorkerStoneItemInItemOutProvider combustionWorkerStoneItemInItemOutProvider = new CombustionWorkerStoneItemInItemOutProvider();
    registrar.registerBodyProvider(combustionWorkerStoneItemInItemOutProvider, TileOvenStone.class);
    registrar.registerBodyProvider(combustionWorkerStoneItemInItemOutProvider, TileKilnStone.class);
    registrar.registerBodyProvider(combustionWorkerStoneItemInItemOutProvider, TileStoneTop.class);

    CombustionWorkerStoneItemInFluidOutProvider combustionWorkerStoneItemInFluidOutProvider = new CombustionWorkerStoneItemInFluidOutProvider();
    registrar.registerBodyProvider(combustionWorkerStoneItemInFluidOutProvider, TileCrucibleStone.class);
    registrar.registerBodyProvider(combustionWorkerStoneItemInFluidOutProvider, TileStoneTop.class);

    CampfireProvider campfireProvider = new CampfireProvider();
    registrar.registerBodyProvider(campfireProvider, TileCampfire.class);

    DryingRackProvider dryingRackProvider = new DryingRackProvider();
    registrar.registerBodyProvider(dryingRackProvider, TileDryingRackBase.class);

    ChoppingBlockProvider choppingBlockProvider = new ChoppingBlockProvider();
    registrar.registerBodyProvider(choppingBlockProvider, TileChoppingBlock.class);

    WoodRackProvider woodRackProvider = new WoodRackProvider();
    registrar.registerBodyProvider(woodRackProvider, TileWoodRack.class);

    GraniteAnvilProvider graniteAnvilProvider = new GraniteAnvilProvider();
    registrar.registerBodyProvider(graniteAnvilProvider, TileGraniteAnvil.class);

    WorktableProvider worktableProvider = new WorktableProvider();
    registrar.registerBodyProvider(worktableProvider, TileWorktable.class);

    StorageItemStackProvider storageItemStackProvider = new StorageItemStackProvider();
    registrar.registerBodyProvider(storageItemStackProvider, TileShelf.class);
    registrar.registerBodyProvider(storageItemStackProvider, TileCrate.class);
    registrar.registerBodyProvider(storageItemStackProvider, TileStash.class);

    CompactingBinProvider compactingBinProvider = new CompactingBinProvider();
    registrar.registerBodyProvider(compactingBinProvider, TileCompactingBin.class);

    SoakingPotProvider soakingPotProvider = new SoakingPotProvider();
    registrar.registerBodyProvider(soakingPotProvider, TileSoakingPot.class);
  }

}
