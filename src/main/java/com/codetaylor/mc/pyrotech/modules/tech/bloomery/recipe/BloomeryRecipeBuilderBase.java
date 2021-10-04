package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BloomeryRecipeBuilderBase<R extends BloomeryRecipeBase<?>, B extends BloomeryRecipeBuilderBase<R, B>> {

  protected final ResourceLocation resourceLocation;
  protected ItemStack output;
  protected Ingredient input;
  protected int burnTimeTicks;
  protected float experience;
  protected float failureChance;
  protected int bloomYieldMin;
  protected int bloomYieldMax;
  protected int slagCount;
  protected ItemStack slagItem;
  protected List<BloomeryRecipeBase.FailureItem> failureItems;
  protected AnvilRecipe.EnumTier[] anvilTiers;
  protected String langKey;

  public BloomeryRecipeBuilderBase(@Nullable ResourceLocation resourceLocation, ItemStack output, Ingredient input) {

    this.resourceLocation = resourceLocation;
    this.output = Preconditions.checkNotNull(output);
    this.input = Preconditions.checkNotNull(input);
    this.burnTimeTicks = 18 * 60 * 20;
    this.failureChance = 0.25f;
    this.bloomYieldMin = 8;
    this.bloomYieldMax = 10;
    this.slagCount = 4;
    this.slagItem = new ItemStack(ModuleTechBloomery.Items.SLAG);
    this.failureItems = new ArrayList<>(1);
    this.anvilTiers = AnvilRecipe.EnumTier.values();
  }

  public B setBurnTimeTicks(int burnTimeTicks) {

    this.burnTimeTicks = burnTimeTicks;
    //noinspection unchecked
    return (B) this;
  }

  public B setExperience(float experience) {

    this.experience = experience;
    //noinspection unchecked
    return (B) this;
  }

  public B setFailureChance(float failureChance) {

    this.failureChance = failureChance;
    //noinspection unchecked
    return (B) this;
  }

  public B setBloomYield(int min, int max) {

    this.bloomYieldMin = min;
    this.bloomYieldMax = max;
    //noinspection unchecked
    return (B) this;
  }

  public B setSlagItem(ItemStack slagItem, int slagCount) {

    this.slagItem = slagItem.copy();
    this.slagItem.setCount(1);
    this.slagCount = slagCount;
    //noinspection unchecked
    return (B) this;
  }

  public B addFailureItem(ItemStack itemStack, int weight) {

    this.failureItems.add(new BloomeryRecipeBase.FailureItem(itemStack, weight));
    //noinspection unchecked
    return (B) this;
  }

  public B setAnvilTiers(AnvilRecipe.EnumTier[] anvilTiers) {

    this.anvilTiers = Arrays.copyOf(anvilTiers, anvilTiers.length);
    //noinspection unchecked
    return (B) this;
  }

  public B setLangKey(@Nullable String langKey) {

    this.langKey = langKey;
    //noinspection unchecked
    return (B) this;
  }

  public abstract R create();
}
