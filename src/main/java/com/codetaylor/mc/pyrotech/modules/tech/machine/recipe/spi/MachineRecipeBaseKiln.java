package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class MachineRecipeBaseKiln<T extends IForgeRegistryEntry<T>>
    extends MachineRecipeItemInItemOutBase<T> {

  protected final float failureChance;
  protected final ItemStack[] failureItems;

  public MachineRecipeBaseKiln(Ingredient input, ItemStack output, int timeTicks, float failureChance, ItemStack[] failureItems) {

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
