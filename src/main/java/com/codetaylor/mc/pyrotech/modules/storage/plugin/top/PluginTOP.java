package com.codetaylor.mc.pyrotech.modules.storage.plugin.top;

import com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider.BagProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new BagProvider());
      return null;
    }
  }
}
