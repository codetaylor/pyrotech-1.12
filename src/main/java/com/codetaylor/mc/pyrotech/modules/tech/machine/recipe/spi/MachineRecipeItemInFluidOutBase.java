package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleFluidOutput;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class MachineRecipeItemInFluidOutBase<T extends IForgeRegistryEntry<T>>
    extends MachineRecipeItemInBase<T>
    implements IRecipeSingleFluidOutput {

  protected final FluidStack output;

  public MachineRecipeItemInFluidOutBase(Ingredient input, FluidStack output, int timeTicks) {

    super(input, timeTicks);

    this.output = output;
  }

  @Override
  public FluidStack getOutput() {

    return this.output.copy();
  }
}
