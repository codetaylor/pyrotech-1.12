package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class MechanicalCompactorCogProcessor
    implements IComponentProcessor {

  private ResourceLocation registryName;

  @Override
  public void setup(IVariableProvider<String> variables) {

    String cog = variables.get("cog");
    ItemStack itemStack = ItemStackUtil.loadStackFromString(cog);
    this.registryName = itemStack.getItem().getRegistryName();
  }

  @Override
  public String process(String key) {

    if (this.registryName != null) {

      if ("progress".equals(key)) {
        return String.valueOf((int) (ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.getCogRecipeProgress(this.registryName) * 100));
      }
    }

    return null;
  }
}
