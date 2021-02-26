package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top;

import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      top.registerProvider(new AnvilProvider(AnvilRecipe.EnumTier.GRANITE));
      top.registerProvider(new AnvilProvider(AnvilRecipe.EnumTier.IRONCLAD));
      top.registerProvider(new CampfireProvider());
      top.registerProvider(new ChoppingBlockProvider());
      top.registerProvider(new CompactingBinProvider());
      top.registerProvider(new DryingRackProvider());
      top.registerProvider(new KilnPitProvider());
      top.registerProvider(new SoakingPotProvider());
      top.registerProvider(new WorktableProvider());
      top.registerProvider(new CompostBinProvider());
      top.registerProvider(new BarrelProvider());
      top.registerProvider(new TanningRackProvider());
      return null;
    }
  }

}
