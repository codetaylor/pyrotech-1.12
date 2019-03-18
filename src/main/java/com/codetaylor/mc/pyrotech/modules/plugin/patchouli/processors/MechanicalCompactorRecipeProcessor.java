package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.MechanicalCompactingBinRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class MechanicalCompactorRecipeProcessor
    extends RecipeProcessorBase<MechanicalCompactingBinRecipe> {

  @Nullable
  @Override
  protected MechanicalCompactingBinRecipe getRecipe(ResourceLocation key) {

    return ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES.getValue(key);
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

    return null;
  }
}
