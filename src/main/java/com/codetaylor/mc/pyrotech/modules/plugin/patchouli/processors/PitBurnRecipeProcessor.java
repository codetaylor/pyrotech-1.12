package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class PitBurnRecipeProcessor
    extends TimedRecipeProcessorBase<PitBurnRecipe> {

  @Nullable
  @Override
  protected PitBurnRecipe getRecipe(ResourceLocation key) {

    return ModuleTechRefractory.Registries.BURN_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input".equals(key)) {
        Block block = this.recipe.getInputMatcher().getBlock();
        ItemStack itemStack = new ItemStack(block);
        return ItemStackUtil.serializeIngredient(Ingredient.fromStacks(itemStack));

      } else if ("output".equals(key)) {
        ItemStack output = this.recipe.getOutput().copy();
        output.setCount(this.recipe.getBurnStages());
        return ItemStackUtil.serializeStack(output);

      } else if ("failure_items".equals(key)) {
        ItemStack[] failureItems = this.recipe.getFailureItems();
        return ItemStackUtil.serializeIngredient(Ingredient.fromStacks(failureItems));

      } else if ("fluid".equals(key)) {
        FluidStack fluidProduced = this.recipe.getFluidProduced();

        if (fluidProduced != null) {
          return fluidProduced.getFluid().getName();
        }
      } else if ("fluid_name".equals(key)) {
        FluidStack fluidProduced = this.recipe.getFluidProduced();

        if (fluidProduced != null) {
          return fluidProduced.getLocalizedName();
        }

      } else if ("fluid_amount".equals(key)) {
        FluidStack fluidProduced = this.recipe.getFluidProduced();

        if (fluidProduced != null) {
          return String.valueOf(fluidProduced.amount * this.recipe.getBurnStages());
        }

      } else if ("refractory_only".equals(key)) {

        return String.valueOf(this.recipe.requiresRefractoryBlocks());
      }
    }

    return super.process(key);
  }
}
