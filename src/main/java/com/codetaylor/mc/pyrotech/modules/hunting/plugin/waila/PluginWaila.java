package com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.provider.ButchersBlockProvider;
import com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.provider.CarcassProvider;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileButchersBlock;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileCarcass;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    registrar.registerBodyProvider(new CarcassProvider(), TileCarcass.class);
    registrar.registerBodyProvider(new ButchersBlockProvider(), TileButchersBlock.class);
  }
}
