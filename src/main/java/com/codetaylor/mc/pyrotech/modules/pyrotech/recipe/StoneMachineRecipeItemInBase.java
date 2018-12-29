package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class StoneMachineRecipeItemInBase<T extends IForgeRegistryEntry<T>>
    extends StoneMachineRecipeBase<T> {

  protected final Ingredient input;

  public StoneMachineRecipeItemInBase(Ingredient input, int timeTicks) {

    super(timeTicks);

    this.input = input;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
