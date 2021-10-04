package com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PitBurnRecipeBuilder {

  private final ItemStack output;
  private final BlockMetaMatcher inputMatcher;

  private int burnStages;
  private int totalBurnTimeTicks;
  private FluidStack fluidProduced;
  private float failureChance;
  private final List<ItemStack> failureItems;
  private boolean requiresRefractoryBlocks;
  private boolean fluidLevelAffectsFailureChance;

  public PitBurnRecipeBuilder(ItemStack output, BlockMetaMatcher inputMatcher) {

    this.output = Preconditions.checkNotNull(output);
    this.inputMatcher = Preconditions.checkNotNull(inputMatcher);

    this.burnStages = 1;
    this.totalBurnTimeTicks = 10 * 60 * 20;
    this.fluidProduced = null;
    this.failureChance = 0;
    this.failureItems = new ArrayList<>();
    this.requiresRefractoryBlocks = false;
    this.fluidLevelAffectsFailureChance = false;
  }

  public PitBurnRecipeBuilder setBurnStages(int burnStages) {

    this.burnStages = burnStages;
    return this;
  }

  public PitBurnRecipeBuilder setTotalBurnTimeTicks(int totalBurnTimeTicks) {

    this.totalBurnTimeTicks = totalBurnTimeTicks;
    return this;
  }

  public PitBurnRecipeBuilder setFluidProduced(@Nullable FluidStack fluidProduced) {

    this.fluidProduced = fluidProduced;
    return this;
  }

  public PitBurnRecipeBuilder setFailureChance(float failureChance) {

    this.failureChance = failureChance;
    return this;
  }

  public PitBurnRecipeBuilder addFailureItem(ItemStack failureItem) {

    this.failureItems.add(Preconditions.checkNotNull(failureItem));
    return this;
  }

  public PitBurnRecipeBuilder setRequiresRefractoryBlocks(boolean requiresRefractoryBlocks) {

    this.requiresRefractoryBlocks = requiresRefractoryBlocks;
    return this;
  }

  public PitBurnRecipeBuilder setFluidLevelAffectsFailureChance(boolean fluidLevelAffectsFailureChance) {

    this.fluidLevelAffectsFailureChance = fluidLevelAffectsFailureChance;
    return this;
  }

  public PitBurnRecipe create(ResourceLocation resourceLocation) {

    return new PitBurnRecipe(
        this.output,
        this.inputMatcher,
        this.burnStages,
        this.totalBurnTimeTicks,
        this.fluidProduced,
        this.failureChance,
        this.failureItems.toArray(new ItemStack[0]),
        this.requiresRefractoryBlocks,
        this.fluidLevelAffectsFailureChance
    ).setRegistryName(resourceLocation);
  }
}
