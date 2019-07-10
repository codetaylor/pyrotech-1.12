package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top;

import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider.AnvilProvider;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider.CampfireProvider;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider.ChoppingBlockProvider;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider.CompactingBinProvider;
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
      return null;
    }
  }

}
