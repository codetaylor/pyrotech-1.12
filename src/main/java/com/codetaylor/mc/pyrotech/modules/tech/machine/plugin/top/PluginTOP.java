package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top;

import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider.*;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new CogWorkerProvider());
      top.registerProvider(new CombustionMachineItemInItemOutProvider());
      top.registerProvider(new CombustionMachineItemInFluidOutProvider());
      top.registerProvider(new MechanicalCompactingBinProvider());
      top.registerProvider(new SawmillProvider());
      return null;
    }
  }
}
