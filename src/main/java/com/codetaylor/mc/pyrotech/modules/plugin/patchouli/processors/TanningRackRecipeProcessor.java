package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.TanningRackRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class TanningRackRecipeProcessor
    extends TimedRecipeProcessorBase<TanningRackRecipe> {

  @Nullable
  @Override
  protected TanningRackRecipe getRecipe(ResourceLocation key) {

    return ModuleTechBasic.Registries.TANNING_RACK_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInputItem());

      } else if ("output".equals(key)) {
        return ItemStackUtil.serializeStack(this.recipe.getOutput());
      }
    }

    return super.process(key);
  }
}
