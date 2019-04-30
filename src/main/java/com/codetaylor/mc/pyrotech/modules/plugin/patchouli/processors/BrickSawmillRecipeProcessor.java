package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickSawmillRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class BrickSawmillRecipeProcessor
    extends TimedRecipeProcessorBase<BrickSawmillRecipe> {

  @Nullable
  @Override
  protected BrickSawmillRecipe getRecipe(ResourceLocation key) {

    return ModuleTechMachine.Registries.BRICK_SAWMILL_RECIPES.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInput());

      } else if ("output".equals(key)) {
        return ItemStackUtil.serializeStack(this.recipe.getOutput());

      } else if ("sawblade".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getBlade());
      }
    }

    return super.process(key);
  }
}
