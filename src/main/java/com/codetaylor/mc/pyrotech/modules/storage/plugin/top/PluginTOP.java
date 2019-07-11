package com.codetaylor.mc.pyrotech.modules.storage.plugin.top;

import com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider.BagProvider;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider.StorageProvider;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.top.provider.TankProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new BagProvider());
      top.registerProvider(new StorageProvider());
      top.registerProvider(new TankProvider());
      return null;
    }
  }
}
