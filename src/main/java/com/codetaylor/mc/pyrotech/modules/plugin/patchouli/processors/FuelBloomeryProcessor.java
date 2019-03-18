package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class FuelBloomeryProcessor
    implements IComponentProcessor {

  private double modifier;

  @Override
  public void setup(IVariableProvider<String> variables) {

    String fuel = variables.get("fuel");
    ItemStack itemStack = ItemStackUtil.loadStackFromString(fuel);
    this.modifier = ModuleTechBloomeryConfig.BLOOMERY.getSpecialFuelBurnTimeModifier(itemStack);
  }

  @Override
  public String process(String key) {

    if ("modifier".equals(key)) {
      return String.valueOf((int) (this.modifier * 100));
    }

    return null;
  }
}
