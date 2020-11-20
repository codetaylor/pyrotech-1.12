package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class BloomeryRecipeBuilder
    extends BloomeryRecipeBuilderBase<BloomeryRecipe, BloomeryRecipeBuilder> {

  public BloomeryRecipeBuilder(ResourceLocation resourceLocation, ItemStack output, Ingredient input) {

    super(resourceLocation, output, input);
  }

  @Override
  public BloomeryRecipe create() {

    BloomeryRecipe recipe = new BloomeryRecipe(
        this.output,
        this.input,
        this.burnTimeTicks,
        this.experience,
        this.failureChance,
        this.bloomYieldMin,
        this.bloomYieldMax,
        this.slagCount,
        this.slagItem,
        this.failureItems.toArray(new BloomeryRecipeBase.FailureItem[0]),
        this.anvilTiers,
        this.langKey
    );

    if (this.resourceLocation != null) {
      recipe.setRegistryName(this.resourceLocation);
    }

    return recipe;
  }
}
