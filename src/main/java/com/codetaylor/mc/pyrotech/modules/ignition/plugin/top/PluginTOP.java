package com.codetaylor.mc.pyrotech.modules.ignition.plugin.top;

import com.codetaylor.mc.pyrotech.modules.ignition.plugin.top.provider.OilLampProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new OilLampProvider());
      return null;
    }
  }
}
