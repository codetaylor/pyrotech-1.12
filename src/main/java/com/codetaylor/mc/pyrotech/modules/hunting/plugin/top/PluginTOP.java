package com.codetaylor.mc.pyrotech.modules.hunting.plugin.top;

import com.codetaylor.mc.pyrotech.modules.hunting.plugin.top.provider.CarcassProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new CarcassProvider());
      return null;
    }
  }

}
