package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.top;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.top.provider.BloomProvider;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.top.provider.BloomeryProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new BloomProvider());
      top.registerProvider(new BloomeryProvider());
      return null;
    }
  }

}
