package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class StoneCrucibleRecipeProcessor
    extends TimedRecipeProcessorBase<StoneCrucibleRecipe> {

  @Nullable
  @Override
  protected StoneCrucibleRecipe getRecipe(ResourceLocation key) {

    return ModuleTechMachine.Registries.STONE_CRUCIBLE_RECIPES.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInput());

      } else if ("output_fluid".equals(key)) {
        return this.recipe.getOutput().getFluid().getName();

      } else if ("output_fluid_name".equals(key)) {
        return this.recipe.getOutput().getLocalizedName();

      } else if ("output_fluid_amount".equals(key)) {
        return String.valueOf(this.recipe.getOutput().amount);
      }
    }

    return super.process(key);
  }
}
