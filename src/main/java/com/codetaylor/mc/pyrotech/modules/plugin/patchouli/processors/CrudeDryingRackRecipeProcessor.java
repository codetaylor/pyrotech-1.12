package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CrudeDryingRackRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class CrudeDryingRackRecipeProcessor
    extends TimedRecipeProcessorBase<CrudeDryingRackRecipe> {

  @Nullable
  @Override
  protected CrudeDryingRackRecipe getRecipe(ResourceLocation key) {

    return ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInput());

      } else if ("output".equals(key)) {
        return ItemStackUtil.serializeStack(this.recipe.getOutput());
      }
    }

    return super.process(key);
  }
}
