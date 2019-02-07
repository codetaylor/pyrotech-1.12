package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class KilnRecipeBase<T extends IForgeRegistryEntry<T>>
    extends StoneMachineRecipeItemInItemOutBase<T> {

  protected final float failureChance;
  protected final ItemStack[] failureItems;

  public KilnRecipeBase(Ingredient input, ItemStack output, int timeTicks, float failureChance, ItemStack[] failureItems) {

    super(input, output, timeTicks);
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.failureItems = failureItems;
  }

  public float getFailureChance() {

    return this.failureChance;
  }

  public ItemStack[] getFailureItems() {

    return this.failureItems;
  }
}
