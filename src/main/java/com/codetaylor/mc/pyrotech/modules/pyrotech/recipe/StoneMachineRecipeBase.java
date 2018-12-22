package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class StoneMachineRecipeBase<T  extends IForgeRegistryEntry<T>>
    extends IForgeRegistryEntry.Impl<T>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  protected final Ingredient input;
  protected final ItemStack output;
  protected final int timeTicks;

  public StoneMachineRecipeBase(Ingredient input, ItemStack output, int timeTicks) {

    this.input = input;
    this.output = output;
    this.timeTicks = timeTicks;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return this.timeTicks;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
