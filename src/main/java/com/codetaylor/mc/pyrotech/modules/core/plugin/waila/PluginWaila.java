package com.codetaylor.mc.pyrotech.modules.core.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.core.block.BlockFreckleberryPlant;
import mcp.mobius.waila.api.IWailaRegistrar;

public class PluginWaila {

  @SuppressWarnings("unused")
  public static void wailaCallback(IWailaRegistrar registrar) {

    registrar.registerStackProvider(new FreckleberryPlantStackProvider(), BlockFreckleberryPlant.class);
  }
}
