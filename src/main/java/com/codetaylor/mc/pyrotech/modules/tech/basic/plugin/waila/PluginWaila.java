package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    registrar.registerBodyProvider(new KilnPitProvider(), TileKilnPit.class);
    registrar.registerBodyProvider(new CampfireProvider(), TileCampfire.class);
    registrar.registerBodyProvider(new DryingRackProvider(), TileDryingRackBase.class);
    registrar.registerBodyProvider(new ChoppingBlockProvider(), TileChoppingBlock.class);
    registrar.registerBodyProvider(new AnvilProvider(AnvilRecipe.EnumTier.GRANITE), TileAnvilGranite.class);
    registrar.registerBodyProvider(new AnvilProvider(AnvilRecipe.EnumTier.IRONCLAD), TileAnvilIronPlated.class);
    registrar.registerBodyProvider(new WorktableProvider(), TileWorktable.class);
    registrar.registerBodyProvider(new CompactingBinProvider(), TileCompactingBin.class);
    registrar.registerBodyProvider(new SoakingPotProvider(), TileSoakingPot.class);
    registrar.registerBodyProvider(new CompostBinProvider(), TileCompostBin.class);
    registrar.registerBodyProvider(new BarrelProvider(), TileBarrel.class);
  }
}
