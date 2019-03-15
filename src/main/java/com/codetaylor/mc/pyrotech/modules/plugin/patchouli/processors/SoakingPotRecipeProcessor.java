package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class SoakingPotRecipeProcessor
    extends TimedRecipeProcessorBase<SoakingPotRecipe> {

  @Nullable
  @Override
  protected SoakingPotRecipe getRecipe(ResourceLocation key) {

    return ModuleTechBasic.Registries.SOAKING_POT_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input_item".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInputItem());

      } else if ("input_fluid".equals(key)) {
        return this.recipe.getInputFluid().getFluid().getName();

      } else if ("output".equals(key)) {
        return ItemStackUtil.serializeStack(this.recipe.getOutput());

      } else if ("input_fluid_name".equals(key)) {
        return this.recipe.getInputFluid().getLocalizedName();

      } else if ("input_fluid_amount".equals(key)) {
        return String.valueOf(this.recipe.getInputFluid().amount);
      }
    }

    return super.process(key);
  }
}
