package com.codetaylor.mc.pyrotech.modules.core.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.core.block.BlockOre;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    {
      OreProvider provider = new OreProvider();
      registrar.registerStackProvider(provider, BlockOre.class);
    }
  }
}
