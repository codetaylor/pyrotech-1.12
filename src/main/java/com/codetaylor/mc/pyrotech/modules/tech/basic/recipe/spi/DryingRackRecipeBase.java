package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.spi;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class DryingRackRecipeBase<T extends IForgeRegistryEntry<T>>
    extends IForgeRegistryEntry.Impl<T>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  protected final Ingredient input;
  protected final ItemStack output;
  protected final int dryTimeTicks;

  public DryingRackRecipeBase(Ingredient input, ItemStack output, int dryTimeTicks) {

    this.input = input;
    this.output = output;
    this.dryTimeTicks = dryTimeTicks;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return this.dryTimeTicks;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
