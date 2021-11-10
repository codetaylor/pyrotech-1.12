package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class BarrelRecipeProcessor
    extends RecipeProcessorBase<BarrelRecipe> {

  @Nullable
  @Override
  protected BarrelRecipe getRecipe(ResourceLocation key) {

    return ModuleTechBasic.Registries.BARREL_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input1".equals(key)) {
        Ingredient[] inputItems = this.recipe.getInputItems();

        if (inputItems.length > 0) {
          return ItemStackUtil.serializeIngredient(inputItems[0]);
        }

      } else if ("input2".equals(key)) {
        Ingredient[] inputItems = this.recipe.getInputItems();

        if (inputItems.length > 1) {
          return ItemStackUtil.serializeIngredient(inputItems[1]);
        }

      } else if ("input3".equals(key)) {
        Ingredient[] inputItems = this.recipe.getInputItems();

        if (inputItems.length > 2) {
          return ItemStackUtil.serializeIngredient(inputItems[2]);
        }

      } else if ("input4".equals(key)) {
        Ingredient[] inputItems = this.recipe.getInputItems();

        if (inputItems.length > 3) {
          return ItemStackUtil.serializeIngredient(inputItems[3]);
        }

      } else if ("input_fluid".equals(key)) {
        return this.recipe.getInputFluid().getFluid().getName();

      } else if ("input_fluid_name".equals(key)) {
        return this.recipe.getInputFluid().getLocalizedName();

      } else if ("input_fluid_amount".equals(key)) {
        return String.valueOf(this.recipe.getInputFluid().amount);

      } else if ("output_fluid".equals(key)) {
        return this.recipe.getOutput().getFluid().getName();

      } else if ("output_fluid_name".equals(key)) {
        return this.recipe.getOutput().getLocalizedName();

      } else if ("output_fluid_amount".equals(key)) {
        return String.valueOf(this.recipe.getOutput().amount);
      }
    }

    return null;
  }
}
