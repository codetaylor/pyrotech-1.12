package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BloomeryRecipeBuilder {

  private final ResourceLocation resourceLocation;
  private ItemStack output;
  private Ingredient input;
  private int burnTimeTicks;
  private float failureChance;
  private int bloomYieldMin;
  private int bloomYieldMax;
  private int slagCount;
  private ItemStack slagItem;
  private List<BloomeryRecipe.FailureItem> failureItems;
  @Nullable
  private String langKey;

  public BloomeryRecipeBuilder(ResourceLocation resourceLocation, ItemStack output, Ingredient input) {

    this.resourceLocation = resourceLocation;
    this.output = Preconditions.checkNotNull(output);
    this.input = Preconditions.checkNotNull(input);
    this.burnTimeTicks = 18 * 60 * 20;
    this.failureChance = 0.25f;
    this.bloomYieldMin = 8;
    this.bloomYieldMax = 10;
    this.slagCount = 4;
    this.slagItem = new ItemStack(ModuleBloomery.Items.SLAG);
    this.failureItems = new ArrayList<>(1);
  }

  public BloomeryRecipeBuilder setBurnTimeTicks(int burnTimeTicks) {

    this.burnTimeTicks = burnTimeTicks;
    return this;
  }

  public BloomeryRecipeBuilder setFailureChance(float failureChance) {

    this.failureChance = failureChance;
    return this;
  }

  public BloomeryRecipeBuilder setBloomYield(int min, int max) {

    this.bloomYieldMin = min;
    this.bloomYieldMax = max;
    return this;
  }

  public BloomeryRecipeBuilder setSlagItem(ItemStack slagItem, int slagCount) {

    this.slagItem = slagItem.copy();
    this.slagItem.setCount(1);
    this.slagCount = slagCount;
    return this;
  }

  public BloomeryRecipeBuilder addFailureItem(ItemStack itemStack, int weight) {

    this.failureItems.add(new BloomeryRecipe.FailureItem(itemStack, weight));
    return this;
  }

  public BloomeryRecipeBuilder setLangKey(@Nullable String langKey) {

    this.langKey = langKey;
    return this;
  }

  public BloomeryRecipe create() {

    return new BloomeryRecipe(
        this.resourceLocation,
        this.output,
        this.input,
        this.burnTimeTicks,
        this.failureChance,
        this.bloomYieldMin,
        this.bloomYieldMax,
        this.slagCount,
        this.slagItem,
        this.failureItems.toArray(new BloomeryRecipe.FailureItem[0]),
        this.langKey
    );
  }
}
