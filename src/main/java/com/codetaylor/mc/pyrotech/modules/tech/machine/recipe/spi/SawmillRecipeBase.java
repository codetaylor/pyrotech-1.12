package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class SawmillRecipeBase<T extends IForgeRegistryEntry<T>>
    extends StoneMachineRecipeItemInItemOutBase<T> {

  protected final Ingredient blade;
  protected final boolean createWoodChips;

  public SawmillRecipeBase(Ingredient input, ItemStack output, int timeTicks, Ingredient blade, boolean createWoodChips) {

    super(input, output, timeTicks);
    this.blade = blade;
    this.createWoodChips = createWoodChips;
  }

  public Ingredient getBlade() {

    return this.blade;
  }

  public boolean createWoodChips() {

    return this.createWoodChips;
  }
}
