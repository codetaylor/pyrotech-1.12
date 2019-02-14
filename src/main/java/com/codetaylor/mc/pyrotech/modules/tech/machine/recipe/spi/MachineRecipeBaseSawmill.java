package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class MachineRecipeBaseSawmill<T extends IForgeRegistryEntry<T>>
    extends MachineRecipeItemInItemOutBase<T> {

  protected final Ingredient blade;
  protected final int woodChips;

  public MachineRecipeBaseSawmill(Ingredient input, ItemStack output, int timeTicks, Ingredient blade, int woodChips) {

    super(input, output, timeTicks);
    this.blade = blade;
    this.woodChips = woodChips;
  }

  public Ingredient getBlade() {

    return this.blade;
  }

  public int getWoodChips() {

    return this.woodChips;
  }
}
