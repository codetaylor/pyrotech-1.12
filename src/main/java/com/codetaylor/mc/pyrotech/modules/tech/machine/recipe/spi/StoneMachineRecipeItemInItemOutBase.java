package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class StoneMachineRecipeItemInItemOutBase<T extends IForgeRegistryEntry<T>>
    extends StoneMachineRecipeItemInBase<T>
    implements IRecipeSingleOutput {

  protected final ItemStack output;

  public StoneMachineRecipeItemInItemOutBase(Ingredient input, ItemStack output, int timeTicks) {

    super(input, timeTicks);

    this.output = output;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

}
