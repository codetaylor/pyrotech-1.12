package com.codetaylor.mc.pyrotech.modules.core.plugin.patchouli;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class DryingRecipeComponentProcessor
    extends TimedRecipeComponentProcessorBase<DryingRackRecipe> {

  @Nullable
  private DryingRackRecipe recipe;

  @Override
  protected void setup(ResourceLocation key) {

    this.recipe = ModuleTechBasic.Registries.DRYING_RACK_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    String result = super.process(key);

    if (result != null) {
      return result;
    }

    if (this.recipe != null) {

      if ("input".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInput());

      } else if ("output".equals(key)) {
        return ItemStackUtil.serializeStack(this.recipe.getOutput());
      }
    }

    return null;
  }
}
